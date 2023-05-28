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

package me.skulduggerry.utilities.bar;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Implementation of bar which is the same for every player
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class GlobalBar extends Bar {

    private final BossBar bossBar;
    private Component title;
    private float progress;

    /**
     * Constructor.
     *
     * @param settings Settings of the bar's looking
     * @param title    Title of the bar
     * @param progress Progress of the bar
     */
    private GlobalBar(@NotNull BarSettings settings, @NotNull Component title, float progress) {
        super(settings);
        this.title = title;
        this.progress = progress;

        bossBar = createBossBar(title, progress);
    }

    /**
     * Get a builder
     *
     * @return The builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Updates the bar for every player
     */
    @Override
    public void update() {
        setBarProperties(bossBar, title, progress);
    }

    /**
     * Add show a new player the bar
     *
     * @param player The player
     */
    @Override
    protected void addToBar(@NotNull Player player) {
        player.showBossBar(bossBar);
    }

    /**
     * Remove a player from the bar
     *
     * @param player The player
     */
    @Override
    protected void removeFromBar(@NotNull Player player) {
        player.hideBossBar(bossBar);
    }

    /**
     * Get the title of the bar
     *
     * @return The current title
     */
    @NotNull
    public Component getTitle() {
        return title;
    }

    /**
     * Set the title of the bar
     *
     * @param title The new title
     */
    public void title(@NotNull Component title) {
        this.title = title;
        update();
    }

    /**
     * Get the progress of the bar.
     *
     * @return The current progress
     */
    public double progress() {
        return progress;
    }

    /**
     * Set the progress of the bar
     *
     * @param progress The new progress
     */
    public void progress(float progress) {
        this.progress = progress;
        update();
    }

    /**
     * Builder class for Bars
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static class Builder extends Bar.Builder<Builder> {

        private Component title;
        private float progress;

        /**
         * Constructor
         */
        private Builder() {
        }

        /**
         * Get the title
         *
         * @return The title
         */
        @NotNull
        public Component title() {
            return title;
        }

        /**
         * Set the title
         *
         * @param title The new title
         * @return The builder
         */
        public Builder title(@NotNull Component title) {
            this.title = title;
            return this;
        }

        /**
         * Get the progress
         *
         * @return The progress
         */
        @Range(from = 0, to = 1)
        public double progress() {
            return progress;
        }

        /**
         * Set the progress
         *
         * @param progress The new progress
         * @return The builder
         */
        public Builder progress(@Range(from = 0, to = 1) float progress) {
            this.progress = progress;
            return this;
        }

        /**
         * Build the bar
         *
         * @return The new bar
         */
        @Override
        public GlobalBar build() {
            return new GlobalBar(settings(), title, progress);
        }
    }
}

