package me.skulduggerry.utilities.scheduler;

import me.skulduggerry.utilities.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Scheduler for repeating tasks.
 * every method can only be scheduled once per instance
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class Scheduler {
    private static final Map<Plugin, Scheduler> instances = new HashMap<>();

    /**
     * Get an instance of the scheduler for the given plugin
     *
     * @param plugin plugin to get the scheduler for
     * @return the scheduler
     */
    public static Scheduler getInstance(@NotNull Plugin plugin) {
        return instances.computeIfAbsent(plugin, Scheduler::new);
    }

    private final Plugin plugin;
    private final Map<Object, Map<String, BukkitTask>> currentTasks;

    /**
     * Constructor.
     *
     * @param plugin plugin to execute tasks
     */
    private Scheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
        currentTasks = new IdentityHashMap<>();
    }

    /**
     * schedules all methods in the object annotated with {@link RepeatingTask}
     *
     * @param instance object to schedule
     */
    public void registerFull(@NotNull Object instance) {
        Arrays.stream(instance.getClass().getDeclaredMethods())
                .filter(method -> ReflectionUtils.hasAnnotation(method, RepeatingTask.class))
                .forEach(method -> registerMethod(instance, method));
    }

    /**
     * register only one method in the object to schedule
     *
     * @param instance   the instance
     * @param methodName the name of the method
     */
    public void registerMethod(@NotNull Object instance, String methodName) {
        Optional<Method> optionalMethod = ReflectionUtils.getMethod(methodName, instance);
        if (optionalMethod.isEmpty()) {
            plugin.getLogger().severe(() -> "Method method %s in class %s not found".formatted(methodName, instance.getClass().getName()));
            return;
        }

        Method method = optionalMethod.get();
        if (!ReflectionUtils.hasAnnotation(method, RepeatingTask.class)) {
            plugin.getLogger().severe(() -> "Methods which should be executed by the scheduler must have the @RepeatingTask-annotation");
            return;
        }

        registerMethod(instance, method);
    }

    /**
     * Register the method and start scheduling
     *
     * @param instance the instance which executes the method
     * @param method   the method to schedule
     */
    private void registerMethod(@NotNull Object instance, Method method) {
        if (currentTasks.containsKey(instance) && currentTasks.get(instance).containsKey(method.getName())) return;

        if (ReflectionUtils.hasParameters(method)) {
            plugin.getLogger().severe(() -> "Methods which should be executed by the scheduler must not have parameters");
            return;
        }

        if (Modifier.isStatic(method.getModifiers())) {
            plugin.getLogger().severe(() -> "Methods which should be executed by the scheduler must not be static");
            return;
        }

        Optional<MethodHandle> optionalMethodHandle = ReflectionUtils.toHandle(method);
        if (optionalMethodHandle.isEmpty()) {
            plugin.getLogger().severe(() -> "Unexpectedly method is not able to be converted into a method handle!");
            return;
        }

        RepeatingTask taskInformation = method.getAnnotation(RepeatingTask.class);
        MethodHandle methodHandle = optionalMethodHandle.get();

        List<MethodHandle> exceptionListeners = getExceptionHandlers(taskInformation);

        Runnable task = () -> {
            try {
                methodHandle.invoke(instance);
            } catch (Throwable exc) {
                TaskHandle handle = new TaskHandle(this, instance, method.getName());
                exceptionListeners.forEach(listener -> {
                    try {
                        listener.invoke(handle, exc);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                });
            }
        };

        boolean sync = taskInformation.sync();
        long delay = taskInformation.delay();
        long period = taskInformation.period();

        BukkitTask handle;
        if (sync) handle = Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
        else handle = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period);

        currentTasks.computeIfAbsent(instance, __ -> new HashMap<>()).put(method.getName(), handle);
    }

    /**
     * get all handlers for exceptions
     *
     * @param repeatingTask information about the task
     * @return list of methods which react on exceptions
     */
    private List<MethodHandle> getExceptionHandlers(RepeatingTask repeatingTask) {
        return Arrays.stream(repeatingTask.listeners())
                .map(ReflectionUtils::getMethods)
                .flatMap(List::stream)
                .filter(method -> ReflectionUtils.hasAnnotation(method, ExceptionListener.class))
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> ReflectionUtils.matchingParameters(method, TaskHandle.class, Throwable.class))
                .map(ReflectionUtils::toHandle)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    /**
     * Removes and stops all running tasks
     */
    public void unregisterPlugin() {
        currentTasks.entrySet().stream()
                .flatMap(objectMapEntry -> objectMapEntry.getValue().values().stream())
                .forEach(BukkitTask::cancel);
        currentTasks.clear();
    }

    /**
     * Unregisters all methods registered from the given object.
     *
     * @param instance The instance
     */
    public void unregisterFull(@NotNull Object instance) {
        if (!currentTasks.containsKey(instance)) return;
        currentTasks.remove(instance).forEach((methodName, taskHandle) -> taskHandle.cancel());
    }

    /**
     * Unregisters the method with the given name that is called on the object.
     *
     * @param instance   The instance
     * @param methodName The method.
     */
    public void unregister(@NotNull Object instance, String methodName) {
        if (!currentTasks.containsKey(instance)) return;
        if (!currentTasks.get(instance).containsKey(methodName)) return;
        currentTasks.get(instance).remove(methodName).cancel();
    }

    /**
     * handle for tasks so listeners can e.g. cancel a task when it throws an exception
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static class TaskHandle {
        private final Scheduler scheduler;
        private final Object instance;
        private final String methodName;
        private boolean cancelled = false;

        /**
         * Constructor
         *
         * @param scheduler  Scheduler which executes the task
         * @param instance   instance which executes the task
         * @param methodName name of the executed method
         */
        private TaskHandle(Scheduler scheduler, Object instance, String methodName) {
            this.scheduler = scheduler;
            this.instance = instance;
            this.methodName = methodName;
        }

        /**
         * @return the instance which executes the method
         */
        public Object getInstance() {
            return instance;
        }

        /**
         * @return name of the executed method
         */
        public String getMethodName() {
            return methodName;
        }

        /**
         * cancel the task
         */
        public void cancel() {
            if (cancelled) return;
            scheduler.unregister(instance, methodName);
            cancelled = true;
        }
    }
}
