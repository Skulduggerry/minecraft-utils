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

import io.papermc.paper.util.Tick;
import me.skulduggerry.utilities.scheduler.ExceptionalRunnable;
import me.skulduggerry.utilities.scheduler.RepeatingTaskHandle;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedList;

/**
 * implementation of the repeating task interface
 *
 * @author Skulduggerry
 * @since 0.2.1
 */
public final class RepeatingTaskHandleImpl extends AbstractBaseTaskHandle implements RepeatingTaskHandle {
    private final Duration period;
    private final ExceptionalRunnable task;
    private final Collection<TaskListener> listeners;

    /**
     * constructor
     *
     * @param plugin the plugin
     * @param id     the task id
     * @param sync   is it sync
     * @param delay  the delay
     * @param period the duration between two executions
     * @param task   the task
     */
    public RepeatingTaskHandleImpl(@NotNull Plugin plugin, @NotNull String id, boolean sync, @NotNull Duration delay, @NotNull Duration period, @NotNull ExceptionalRunnable task) {
        super(plugin, id, sync, delay);
        this.period = period;
        this.task = task;
        listeners = new LinkedList<>();

        setHandle(createHandle());
    }

    /**
     * create the task handle
     *
     * @return the task handle
     */
    @NotNull
    private BukkitTask createHandle() {
        Runnable runnable = () -> {
            try {
                task.run();
            } catch (Exception e) {
                setLastException(e);
                listeners.forEach(taskListener -> taskListener.onException(this, e));
            }
        };

        if (isSync())
            return Bukkit.getScheduler().runTaskTimer(getPlugin(), runnable, getDelay().get(Tick.tick()), period.get(Tick.tick()));
        return Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), runnable, getDelay().get(Tick.tick()), period.get(Tick.tick()));
    }

    /**
     * cancel the task
     */
    @Override
    public void cancel() {
        super.cancel();
        listeners.forEach(taskListener -> taskListener.onCancel(this));
    }

    /**
     * the duration between two executions
     *
     * @return the period
     */
    @NotNull
    @Override
    public Duration getPeriod() {
        return period;
    }

    /**
     * @return the task
     */
    @NotNull
    @Override
    public ExceptionalRunnable getTask() {
        return task;
    }

    /**
     * Adds a listener
     *
     * @param listener the listener
     * @return the task handle
     */
    @Override
    public RepeatingTaskHandle addListener(@NotNull TaskListener listener) {
        listeners.add(listener);
        if (isCancelled()) listener.onCancel(this);
        if (getLastException() != null) listener.onException(this, getLastException());
        return this;
    }
}
