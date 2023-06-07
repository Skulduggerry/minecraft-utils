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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * task handle that manages a repeating called task
 *
 * @author Skulduggerry
 * @since 0.2.1
 */
public interface RepeatingTaskHandle extends BaseTaskHandle {

    /**
     * the duration between two executions
     *
     * @return the period
     */
    @NotNull
    Duration getPeriod();

    /**
     * @return the task
     */
    @NotNull
    ExceptionalRunnable getTask();

    /**
     * Adds a listener
     *
     * @param listener the listener
     * @return the task handle
     */
    RepeatingTaskHandle addListener(@NotNull TaskListener listener);

    /**
     * adds a cancel listener
     *
     * @param handler the listener
     * @return the task handle
     */
    default RepeatingTaskHandle onCancel(@NotNull Consumer<RepeatingTaskHandle> handler) {
        return addListener(new TaskListener() {
            @Override
            public void onCancel(@NotNull RepeatingTaskHandle task) {
                handler.accept(task);
            }
        });
    }

    /**
     * adds an exception listener
     *
     * @param handler the listener
     * @return the task handle
     */
    default RepeatingTaskHandle onException(@NotNull BiConsumer<RepeatingTaskHandle, Exception> handler) {
        return addListener(new TaskListener() {
            @Override
            public void onException(@NotNull RepeatingTaskHandle task, @NotNull Exception exc) {
                handler.accept(task, exc);
            }
        });
    }

    /**
     * a listener for task events
     *
     * @author Skulduggerry
     * @since 0.2.1
     */
    interface TaskListener {

        /**
         * called when the task got cancelled
         *
         * @param task the task
         */
        default void onCancel(@NotNull RepeatingTaskHandle task) {
        }

        /**
         * called when the task throws an exception
         *
         * @param task the task
         * @param exc  the exception
         */
        default void onException(@NotNull RepeatingTaskHandle task, @NotNull Exception exc) {
        }
    }

    /**
     * marks a method as task that can be run repetitively.
     * the task must not be static and must have no parameters.
     *
     * @author Skulduggerry
     * @since 0.2.1
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface RepeatingTask {

        /**
         * the id if the task
         *
         * @return the id
         */
        @NotNull
        String id();

        /**
         * @return is it running synchronously
         */
        boolean sync() default true;

        /**
         * delay before first execution (in ticks)
         *
         * @return the delay
         */
        @Range(from = 0, to = Long.MAX_VALUE)
        long delay() default 0;

        /**
         * the duration between two executions (in ticks)
         *
         * @return the period
         */
        @Range(from = 0, to = Long.MAX_VALUE)
        long period() default 20;

        /**
         * list of classes that contain a {@link CancelHandler} for this task
         *
         * @return the cancel handlers
         */
        Class<?>[] cancelHandlers() default {};

        /**
         * list of classes that contain a {@link ExceptionHandler} for this task
         *
         * @return the exception handlers
         */
        Class<?>[] exceptionHandlers() default {};
    }

    /**
     * marks a method as handler for cancel events on {@link RepeatingTask}.
     * the method must be static and take a {@link RepeatingTaskHandle} as parameter.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface CancelHandler {
    }

    /**
     * marks a method as handler for exception events on {@link RepeatingTask}.
     * the method must be static and take a {@link RepeatingTaskHandle} and a {@link Exception} as parameter.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface ExceptionHandler {
    }
}
