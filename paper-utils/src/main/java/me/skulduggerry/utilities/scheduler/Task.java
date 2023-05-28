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

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Class to run a given task once.
 *
 * @param <V> Type of the result
 * @author Skulduggerry
 * @since 0.1.0
 */
public class Task<V> {

    private final String name;
    private final Plugin plugin;
    private final Callable<V> task;
    private final boolean sync;
    private final long delay;
    private boolean finished = false;
    private Throwable thrownException;
    private V result;
    private BukkitTask handle;
    private final Collection<TaskListener<V>> listeners = new LinkedList<>();

    /**
     * Constructor.
     *
     * @param name   Name of the task.
     * @param plugin Plugin to run the task
     * @param task   The function to produce a result
     * @param sync   Should the task be executed synchronously
     * @param delay  Delay before it executes
     */
    private Task(@NotNull String name, @NotNull Plugin plugin, @NotNull Callable<V> task, boolean sync, @Range(from = 0, to = Long.MAX_VALUE) long delay) {
        this.name = name;
        this.plugin = plugin;
        this.task = task;
        this.sync = sync;
        this.delay = delay;
    }

    /**
     * Get the name of the task
     *
     * @return The name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @return whether the task was started
     */
    public boolean isStarted() {
        return handle != null;
    }

    /**
     * @return whether the task was cancelled
     */
    public boolean isCancelled() {
        return isStarted() && handle.isCancelled();
    }

    /**
     * @return whether the task has finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Starts the task of it was not already started
     */
    public void start() {
        if (isStarted()) return;

        Runnable runnable = () -> {
            try {
                result = task.call();
                finished = true;
                listeners.forEach(listener -> listener.onComplete(this, result));
            } catch (Throwable exc) {
                thrownException = exc;
                listeners.forEach(listener -> listener.onException(this, exc));
            }
        };

        if (sync)
            if (delay <= 0) handle = Bukkit.getScheduler().runTask(plugin, runnable);
            else handle = Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        else if (delay <= 0) handle = Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        else handle = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    /**
     * Cancels the task if it is already running
     */
    public void cancel() {
        if (!isStarted() || isCancelled() || isFinished() || thrownException != null) return;
        handle.cancel();
        listeners.forEach(listener -> listener.onCancel(this));
    }

    /**
     * Add a listener to react on the tasks actions
     *
     * @param listener The listener
     * @return The task
     */
    public Task<V> addListener(@NotNull TaskListener<V> listener) {
        listeners.add(listener);
        if (isFinished()) listener.onComplete(this, result);
        if (isCancelled()) listener.onCancel(this);
        if (thrownException != null) listener.onException(this, thrownException);
        return this;
    }

    /**
     * Adds a listener which reacts on a complete event
     *
     * @param handler The handler when the task finishes
     * @return The task
     */
    public Task<V> onComplete(@NotNull BiConsumer<Task<V>, V> handler) {
        return addListener(new TaskListener<>() {
            @Override
            public void onComplete(@NotNull Task<V> task, @Nullable V result) {
                handler.accept(task, result);
            }
        });
    }

    /**
     * Adds a listener which reacts on a cancel event
     *
     * @param handler The handler called when the task gets cancelled
     * @return The task
     */
    public Task<V> onCancel(Consumer<Task<V>> handler) {
        return addListener(new TaskListener<>() {
            @Override
            public void onCancel(@NotNull Task<V> task) {
                handler.accept(task);
            }
        });
    }

    /**
     * Adds a listener which reacts on an exception
     *
     * @param handler The handler called when the task throws an exception
     * @return The task
     */
    public Task<V> onException(BiConsumer<Task<V>, Throwable> handler) {
        return addListener(new TaskListener<>() {
            @Override
            public void onException(@NotNull Task<V> task, @NotNull Throwable exc) {
                handler.accept(task, exc);
            }
        });
    }

    /**
     * Get a new builder.
     *
     * @param plugin Plugin to run the task
     * @param task   Task to run
     * @param <V>    return type of the task
     * @return the new builder
     */
    public static <V> Builder<V> builder(@NotNull Plugin plugin, @NotNull Callable<V> task) {
        return new Builder<>(plugin, task);
    }

    /**
     * Builder class for tasks
     *
     * @param <V> Type of the result
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static class Builder<V> {
        private static int taskNumber = 0;
        private String name = String.valueOf("Task-%s".formatted(taskNumber++));
        private final Plugin plugin;
        private final Callable<V> task;
        private boolean sync = true;
        private long delay = 0;

        /**
         * Constructor
         *
         * @param plugin Plugin to run the task
         * @param task   task to run
         */
        private Builder(@NotNull Plugin plugin, @NotNull Callable<V> task) {
            this.plugin = plugin;
            this.task = task;
        }

        /**
         * Set the name of the task
         *
         * @param name The new name
         * @return the builder
         */
        public Builder<V> name(@NotNull String name) {
            this.name = name;
            return this;
        }

        /**
         * Makes the task sync or async
         *
         * @param sync new run method
         * @return the builder
         */
        public Builder<V> sync(boolean sync) {
            this.sync = sync;
            return this;
        }

        /**
         * Set the delay the task waits before executing
         *
         * @param delay The new delay
         * @return The builder
         */
        public Builder<V> delay(@Range(from = 0, to = Long.MAX_VALUE) long delay) {
            this.delay = delay;
            return this;
        }

        /**
         * Build the new task
         *
         * @return the builder
         */
        @NotNull
        public Task<V> build() {
            return new Task<>(name, plugin, task, sync, delay);
        }
    }
}

