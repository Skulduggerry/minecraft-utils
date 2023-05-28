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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Listener for actions that might happen to a task.
 *
 * @param <V> Type of the result.
 * @author Skulduggerry
 * @since 0.1.0
 */
public interface TaskListener<V> {

    /**
     * Called when tha task has finished.
     *
     * @param task   Task which completed.
     * @param result Result the task has produced.
     */
    default void onComplete(@NonNull Task<V> task, @Nullable V result) {
    }

    /**
     * Called when someone uses {@link Task#cancel()} while it is running.
     *
     * @param task Task which is cancelled.
     */
    default void onCancel(@NonNull Task<V> task) {
    }

    /**
     * Called when the task produces an exception.
     * Automatically stops the task.
     *
     * @param task Task which produced the exception.
     * @param exc  The exception.
     */
    default void onException(@NonNull Task<V> task, @NonNull Throwable exc) {
    }
}
