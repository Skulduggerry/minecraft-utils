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
