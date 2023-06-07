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

package me.skulduggerry.utilities.scheduler.implementation;

import me.skulduggerry.utilities.scheduler.BaseTaskHandle;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Objects;

/**
 * implementation of the base task interface
 *
 * @author Skulduggerry
 * @since 0.2.1
 */
public abstract class AbstractBaseTaskHandle implements BaseTaskHandle {

    private final Plugin plugin;
    private final String id;
    private final boolean sync;
    private final Duration delay;
    private boolean cancelled;
    private Exception lastException;
    private BukkitTask handle;

    /**
     * constructor
     *
     * @param plugin the plugin
     * @param id     the task id
     * @param sync   is it sync
     * @param delay  the delay
     */
    public AbstractBaseTaskHandle(@NotNull Plugin plugin, @NotNull String id, boolean sync, @NotNull Duration delay) {
        this.plugin = plugin;
        this.id = id;
        this.sync = sync;
        this.delay = delay;
    }

    /**
     * get the plugin that owns this task
     *
     * @return the plugin
     */
    @NotNull
    @Override
    public final Plugin getPlugin() {
        return plugin;
    }

    /**
     * get the id of the task
     *
     * @return the id
     */
    @NotNull
    @Override
    public final String getId() {
        return id;
    }

    /**
     * is the task running synchronously
     *
     * @return is sync
     */
    @Override
    public final boolean isSync() {
        return sync;
    }

    /**
     * get the delay before first execution
     *
     * @return the delay
     */
    @NotNull
    @Override
    public final Duration getDelay() {
        return delay;
    }

    /**
     * is the task cancelled
     *
     * @return cancelled
     */
    @Override
    public final boolean isCancelled() {
        return cancelled;
    }

    /**
     * cancel the task
     */
    @Override
    public void cancel() {
        if (isCancelled() || getLastException() != null) return;
        cancelled = true;
        Objects.requireNonNull(handle, "Handle must have been set to be able to cancel a task.").cancel();
    }

    /**
     * get the last exception
     *
     * @return the last exception
     */
    @Override
    public final @Nullable Exception getLastException() {
        return lastException;
    }

    /**
     * set the last thrown exception
     *
     * @param lastException the last exception
     */
    protected final void setLastException(@NotNull Exception lastException) {
        this.lastException = lastException;
    }

    /**
     * set the task handle
     *
     * @param handle the handle
     */
    protected final void setHandle(@NotNull BukkitTask handle) {
        if (this.handle != null)
            throw new UnsupportedOperationException("Cannot redefine handle for tasks");
        this.handle = handle;
    }
}
