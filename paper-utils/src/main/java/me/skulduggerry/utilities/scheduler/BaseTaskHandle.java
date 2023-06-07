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

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

/**
 * Interface for task handle
 *
 * @author Skulduggerry
 * @since 0.2.1
 */
public interface BaseTaskHandle {

    /**
     * get the plugin that owns this task
     *
     * @return the plugin
     */
    @NotNull
    Plugin getPlugin();

    /**
     * get the id of the task
     *
     * @return the id
     */
    @NotNull
    String getId();

    /**
     * is the task running synchronously
     *
     * @return is sync
     */
    boolean isSync();

    /**
     * get the delay before first execution
     *
     * @return the delay
     */
    @NotNull
    Duration getDelay();

    /**
     * is the task cancelled
     *
     * @return cancelled
     */
    boolean isCancelled();

    /**
     * cancel the task
     */
    void cancel();

    /**
     * get the last exception
     *
     * @return the last exception
     */
    @Nullable
    Exception getLastException();
}
