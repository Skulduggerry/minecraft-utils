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

package me.skulduggerry.utilities.utils;

import me.skulduggerry.utilities.scheduler.RepeatingTaskHandle;
import me.skulduggerry.utilities.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Helper class for the scheduler
 *
 * @author Skulduggerry
 * @since 0.2.1
 */
public final class SchedulerUtils {

    /**
     * Constructor
     */
    private SchedulerUtils() {
    }

    /**
     * Get all listeners for a {@link RepeatingTaskHandle} that is called using a {@link Scheduler#registerFull(Object)} or {@link Scheduler#registerMethod(Object, String)}.
     * If a listener cannot be registered for example because it has the wring signature it fails silently and will not be registered.
     * If a listener throws an exception while executing the stacktrace will be printed to the console to ensure that all other listeners can also run.
     *
     * @param taskInformation The information about the task containing the listener classes.
     * @return The collection of listeners.
     */
    public static Collection<RepeatingTaskHandle.TaskListener> getListeners(@NotNull RepeatingTaskHandle.RepeatingTask taskInformation) {
        Collection<RepeatingTaskHandle.TaskListener> listeners = new LinkedList<>();

        for (Class<?> exceptionHandler : taskInformation.exceptionHandlers()) {
            ReflectionUtils.getDeclaredMethods(exceptionHandler)
                    .stream()
                    .filter(SchedulerUtils::isExceptionHandler)
                    .map(method -> new RepeatingTaskHandle.TaskListener() {
                        @Override
                        public void onException(@NotNull RepeatingTaskHandle task, @NotNull Exception exc) {
                            try {
                                method.invoke(null, task, exc);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .forEach(listeners::add);
        }

        for (Class<?> cancelHandler : taskInformation.cancelHandlers()) {
            ReflectionUtils.getDeclaredMethods(cancelHandler)
                    .stream()
                    .filter(SchedulerUtils::isCancelHandler)
                    .map(method -> new RepeatingTaskHandle.TaskListener() {
                        @Override
                        public void onCancel(@NotNull RepeatingTaskHandle task) {
                            try {
                                method.invoke(null, task);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .forEach(listeners::add);
        }

        return listeners;
    }

    /**
     * Checks if the given method fulfills the requirements to be a {@link RepeatingTaskHandle.ExceptionHandler}
     *
     * @param method The method
     * @return The result of the check
     */
    public static boolean isExceptionHandler(@NotNull Method method) {
        return ReflectionUtils.isStatic(method) && ReflectionUtils.hasAnnotation(method, RepeatingTaskHandle.ExceptionHandler.class) && ReflectionUtils.matchingParametersExact(method, RepeatingTaskHandle.class, Exception.class);
    }

    /**
     * Checks if the given method fulfills the requirements to be a {@link RepeatingTaskHandle.CancelHandler}
     *
     * @param method The method
     * @return The result of the check
     */
    public static boolean isCancelHandler(@NotNull Method method) {
        return ReflectionUtils.isStatic(method) && ReflectionUtils.hasAnnotation(method, RepeatingTaskHandle.CancelHandler.class) && ReflectionUtils.matchingParametersExact(method, RepeatingTaskHandle.class);
    }

    /**
     * Checks if the given method fulfills the requirements to be a {@link RepeatingTaskHandle.RepeatingTask}
     *
     * @param method The method
     * @return The result of the check
     */
    public static boolean isRepeatingTask(@NotNull Method method) {
        return !ReflectionUtils.isStatic(method) && ReflectionUtils.hasAnnotation(method, RepeatingTaskHandle.RepeatingTask.class) && ReflectionUtils.matchingParametersExact(method);
    }
}
