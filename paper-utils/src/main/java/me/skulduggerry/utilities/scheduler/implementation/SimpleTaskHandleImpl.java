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
import me.skulduggerry.utilities.scheduler.Scheduler;
import me.skulduggerry.utilities.scheduler.SimpleTaskHandle;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;

public final class SimpleTaskHandleImpl<V> extends AbstractBaseTaskHandle implements SimpleTaskHandle<V> {

    private final Callable<V> task;
    private V result;
    private boolean finished;
    private final Collection<TaskListener<V>> listeners;

    /**
     * constructor
     *
     * @param plugin the plugin
     * @param id     the task id
     * @param sync   is it sync
     * @param delay  the delay
     * @param task   te task
     */
    public SimpleTaskHandleImpl(@NotNull Plugin plugin, @NotNull String id, boolean sync, @NotNull Duration delay, @NotNull Callable<V> task) {
        super(plugin, id, sync, delay);
        this.task = task;
        listeners = new LinkedList<>();

        setHandle(createHandle());
    }

    /**
     * create the task handle
     * @return the task handle
     */
    @NotNull
    private BukkitTask createHandle() {
        Runnable runnable = () -> {
            try {
                result = task.call();
                finished = true;
                listeners.forEach(vTaskListener -> vTaskListener.onComplete(this, result));
            } catch (Exception e) {
                setLastException(e);
                listeners.forEach(vTaskListener -> vTaskListener.onException(this, e));
            }
        };

        if (isSync()) {
            if (getDelay().isZero()) return Bukkit.getScheduler().runTask(getPlugin(), runnable);
            return Bukkit.getScheduler().runTaskLater(getPlugin(), runnable, getDelay().get(Tick.tick()));
        }
        if (getDelay().isZero()) return Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), runnable);
        return Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(), runnable, getDelay().get(Tick.tick()));
    }

    /**
     * @return the task to execute
     */
    @Override
    public Callable<V> getTask() {
        return task;
    }

    /**
     * @return is execution finished
     */
    @Override
    public boolean isFinished() {
        return finished;
    }

    /**
     * add a listener for task events
     *
     * @param listener the listener
     * @return the task handle
     */
    @Override
    public SimpleTaskHandle<V> addListener(@NotNull TaskListener<V> listener) {
        listeners.add(listener);
        if (isFinished()) listener.onComplete(this, result);
        if (isCancelled()) listener.onCancel(this);
        if (getLastException() != null) listener.onException(this, getLastException());
        return this;
    }

    /**
     * cancel the task
     */
    @Override
    public void cancel() {
        if (isFinished()) return;
        super.cancel();
        listeners.forEach(vTaskListener -> vTaskListener.onCancel(this));
    }
}
