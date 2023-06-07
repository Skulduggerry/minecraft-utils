/*
 * MIT License
 *
 * Copyright (c) 2023 Skulduggerry
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.skulduggerry.utilities.scheduler;

import io.papermc.paper.util.Tick;
import me.skulduggerry.utilities.collection.Pair;
import me.skulduggerry.utilities.scheduler.implementation.RepeatingTaskHandleImpl;
import me.skulduggerry.utilities.scheduler.implementation.SimpleTaskHandleImpl;
import me.skulduggerry.utilities.utils.ReflectionUtils;
import me.skulduggerry.utilities.utils.SchedulerUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;


/**
 * Scheduler for repeating tasks.
 * every method can only be scheduled once per instance
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class Scheduler {
    private static final Map<Plugin, Scheduler> instances = new HashMap<>();

    public static Scheduler getInstance(@NotNull Plugin plugin) {
        return instances.computeIfAbsent(plugin, Scheduler::new);
    }

    private final Plugin plugin;
    private final Map<Pair<Object, String>, RepeatingTaskHandle> registeredTasks;
    private boolean doesFullUnregister;

    /**
     * Constructor
     *
     * @param plugin The plugin the scheduler is working for.
     */
    private Scheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
        registeredTasks = new HashMap<>();
        doesFullUnregister = false;
    }

    /**
     * Schedules all methods in the object annotated with {@link RepeatingTaskHandle.RepeatingTask} that fit
     * the requirements.
     *
     * @param instance object to schedule
     */
    public void registerFull(@NotNull Object instance) {
        ReflectionUtils.getDeclaredMethods(instance.getClass())
                .stream()
                .filter(method -> ReflectionUtils.hasAnnotation(method, RepeatingTaskHandle.RepeatingTask.class))
                .forEach(method -> registerMethod(instance, method));
    }

    /**
     * Register the method with the given name to be called on the given object to be scheduled.
     * The method must fulfill all requirements described in {@link RepeatingTaskHandle.RepeatingTask} to be registered.
     *
     * @param instance   the instance
     * @param methodName the name of the method
     */
    public void registerMethod(@NotNull Object instance, @NotNull String methodName) {
        Optional<Method> optionalMethod = ReflectionUtils.getMethod(instance.getClass(), methodName);

        //check if the method exists
        if (optionalMethod.isEmpty()) {
            plugin.getLogger().warning(() -> "Cannot register method '%s' because the method in the class '%s' does not exist or has the not the correct signature!".formatted(methodName, instance.getClass().getName()));
            return;
        }

        registerMethod(instance, optionalMethod.get());
    }

    /**
     * Register a method to schedule.
     * The method must fulfill all requirements described in {@link RepeatingTaskHandle.RepeatingTask} to be registered.
     *
     * @param instance The instance to call the method on.
     * @param method   The method.
     */
    private void registerMethod(@NotNull Object instance, @NotNull Method method) {
        Pair<Object, String> bindingInformation = Pair.makePair(instance, method.getName());

        //check if the method is already registered on this object
        if (registeredTasks.containsKey(bindingInformation)) {
            plugin.getLogger().info(() -> "The method '%s' is already registered on the object '%s'".formatted(method.getName(), instance));
            return;
        }

        //checks if the requirements are fulfilled
        if (!SchedulerUtils.isRepeatingTask(method)) {
            plugin.getLogger().warning(() -> "Cannot register method '%s' from class '%s' because it does not fulfill all requirements.".formatted(method.getName(), instance.getClass().getName()));
            return;
        }

        RepeatingTaskHandle.RepeatingTask taskInformation = ReflectionUtils.getAnnotation(method, RepeatingTaskHandle.RepeatingTask.class).orElseThrow(() -> new Error("Missing annotation @RepeatingTask event though it was present before"));

        //makes the Method to a MethodHandle
        Optional<MethodHandle> optionalMethodHandle = ReflectionUtils.toHandle(method);
        if (optionalMethodHandle.isEmpty()) {
            plugin.getLogger().severe(() -> "Error while trying to convert the method '%s' from class '%s' to a MethodHandle");
            return;
        }
        MethodHandle methodHandle = optionalMethodHandle.get();

        //get all information
        String id = taskInformation.id();
        boolean sync = taskInformation.sync();
        Duration delay = Tick.of(taskInformation.delay());
        Duration period = Tick.of(taskInformation.period());
        ExceptionalRunnable runnable = () -> {
            try {
                methodHandle.invoke(instance);
            } catch (Exception e) {
                throw e;
            } catch (Throwable e) {
                throw new Exception(e);
            }
        };

        //register the task
        RepeatingTaskHandle taskHandle = register(id, sync, delay, period, runnable);
        SchedulerUtils.getListeners(taskInformation).forEach(taskHandle::addListener);

        //add it to the map of tasks
        registeredTasks.put(bindingInformation, taskHandle);
    }

    /**
     * cancels all tasks and remove the plugin from the collection of schedulers.
     * should only be called when the plugin shuts down
     */
    public void unregisterPlugin() {
        unregisterAll();
        instances.remove(plugin);
    }

    /**
     * unregisters all tasks
     */
    public void unregisterAll() {
        if (doesFullUnregister) return;

        doesFullUnregister = true;
        registeredTasks.values().forEach(RepeatingTaskHandle::cancel);
        doesFullUnregister = false;
        registeredTasks.clear();
    }

    /**
     * unregisters all tasks that are registered to the given object
     *
     * @param instance the object
     */
    public void unregisterFull(@NotNull Object instance) {
        if (doesFullUnregister) return;

        doesFullUnregister = true;
        List<String> methods = new LinkedList<>();
        registeredTasks.entrySet().stream()
                .filter(entry -> entry.getKey().first.equals(instance))
                .forEach(entry -> {
                    entry.getValue().cancel();
                    methods.add(entry.getKey().second);
                });
        doesFullUnregister = false;
        methods.forEach(s -> registeredTasks.remove(Pair.makePair(instance, s)));
    }

    /**
     * unregister a method called on the given object
     *
     * @param instance The object
     * @param method   the method name
     */
    public void unregisterMethod(@NotNull Object instance, @NotNull String method) {
        if (doesFullUnregister) return;

        Pair<Object, String> bindingInformation = Pair.makePair(instance, method);
        RepeatingTaskHandle taskHandle = registeredTasks.get(bindingInformation);
        if (taskHandle != null) {
            taskHandle.cancel();
            registeredTasks.remove(bindingInformation);
        }
    }

    /**
     * registers a repeating task
     *
     * @param id     the id of the task
     * @param period the duration between two executions
     * @param task   the task
     * @return the task handle
     */
    public RepeatingTaskHandle register(@NotNull String id, @NotNull Duration period, @NotNull ExceptionalRunnable task) {
        return new RepeatingTaskHandleImpl(plugin, id, true, Duration.ZERO, period, task);
    }

    /**
     * registers a repeating task
     *
     * @param id     the id of the task
     * @param sync   is it running synchronously
     * @param delay  the delay before first execute
     * @param period the duration between two executions
     * @param task   the task
     * @return the task handle
     */
    public RepeatingTaskHandle register(@NotNull String id, boolean sync, @NotNull Duration delay, @NotNull Duration period, @NotNull ExceptionalRunnable task) {
        return new RepeatingTaskHandleImpl(plugin, id, sync, delay, period, task);
    }

    /**
     * registers a task
     *
     * @param id   the id of the task
     * @param task the task
     * @param <V>  the result type
     * @return the task handle
     */
    public <V> SimpleTaskHandle<V> call(@NotNull String id, @NotNull Callable<V> task) {
        return new SimpleTaskHandleImpl<>(plugin, id, true, Duration.ZERO, task);
    }

    /**
     * registers a task
     *
     * @param id    the id of the task
     * @param sync  is it running synchronously
     * @param delay the delay before first execute
     * @param task  the task
     * @param <V>   the result type
     * @return the task handle
     */
    public <V> SimpleTaskHandle<V> call(@NotNull String id, boolean sync, @NotNull Duration delay, @NotNull Callable<V> task) {
        return new SimpleTaskHandleImpl<>(plugin, id, sync, delay, task);
    }
}
