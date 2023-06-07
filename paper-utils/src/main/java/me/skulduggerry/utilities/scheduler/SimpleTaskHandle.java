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
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * a task that is called once and produces a result
 *
 * @param <V> the result type
 * @author Skulduggerry
 * @since 0.2.1
 */
public interface SimpleTaskHandle<V> extends BaseTaskHandle {

    /**
     * @return the task to execute
     */
    Callable<V> getTask();

    /**
     * @return is execution finished
     */
    boolean isFinished();

    /**
     * add a listener for task events
     *
     * @param listener the listener
     * @return the task handle
     */
    SimpleTaskHandle<V> addListener(@NotNull TaskListener<V> listener);

    /**
     * add a complete listener
     *
     * @param handler the listener
     * @return the task handle
     */
    default SimpleTaskHandle<V> onComplete(@NotNull BiConsumer<SimpleTaskHandle<V>, V> handler) {
        return addListener(new TaskListener<>() {
            @Override
            public void onComplete(@NotNull SimpleTaskHandle<V> task, @Nullable V result) {
                handler.accept(task, result);
            }
        });
    }

    /**
     * add a cancel listener
     *
     * @param handler the listener
     * @return the task handle
     */
    default SimpleTaskHandle<V> onCancel(@NotNull Consumer<SimpleTaskHandle<V>> handler) {
        return addListener(new TaskListener<>() {
            @Override
            public void onCancel(@NotNull SimpleTaskHandle<V> task) {
                handler.accept(task);
            }
        });
    }

    /**
     * add a exception listener
     *
     * @param handler the listener
     * @return the task handle
     */
    default SimpleTaskHandle<V> onException(@NotNull BiConsumer<SimpleTaskHandle<V>, Exception> handler) {
        return addListener(new TaskListener<>() {
            @Override
            public void onException(@NotNull SimpleTaskHandle<V> task, @NotNull Exception exc) {
                handler.accept(task, exc);
            }
        });
    }

    /**
     * a listener for task events
     *
     * @param <V> the result type
     * @author Skulduggerry
     * @since 0.2.1
     */
    interface TaskListener<V> {

        default void onComplete(SimpleTaskHandle<V> task, V result) {

        }

        default void onCancel(SimpleTaskHandle<V> task) {

        }

        default void onException(SimpleTaskHandle<V> task, Exception exc) {

        }
    }
}
