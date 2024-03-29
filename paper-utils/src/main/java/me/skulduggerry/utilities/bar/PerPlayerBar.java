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

import me.skulduggerry.utilities.template.message.MessageTemplate;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of bar which is different for every player
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class PerPlayerBar extends Bar {

    private final Map<Player, BossBar> playerBars;
    private final MessageTemplate titleTemplate;
    private final ProgressFunction progressFunction;

    /**
     * Constructor
     *
     * @param settings         Settings for the BossBar
     * @param titleTemplate    The function to generate the title
     * @param progressFunction The progress
     */
    private PerPlayerBar(@NotNull BarSettings settings, @NotNull MessageTemplate titleTemplate, @NotNull ProgressFunction progressFunction) {
        super(settings);
        playerBars = new HashMap<>();
        this.titleTemplate = titleTemplate;
        this.progressFunction = progressFunction;
    }

    /**
     * Updates the bar for every player
     */
    @Override
    public void update() {
        getActivePlayers().forEach(this::update);
    }

    /**
     * Updates the bar of the given player
     *
     * @param player The player
     */
    public void update(@NotNull Player player) {
        if (!playerBars.containsKey(player)) return;

        BossBar bar = playerBars.get(player);
        Component title = titleTemplate.getMessage(player);
        float progress = progressFunction.getProgress(player);

        setBarProperties(bar, title, progress);
    }

    /**
     * Add show a new player the bar
     *
     * @param player The player
     */
    @Override
    protected void addToBar(@NotNull Player player) {
        if (playerBars.containsKey(player)) return;

        Component title = titleTemplate.getMessage(player);
        float progress = progressFunction.getProgress(player);

        BossBar bossBar = createBossBar(title, progress);
        player.showBossBar(bossBar);
        playerBars.put(player, bossBar);
    }

    /**
     * Remove a player from the bar
     *
     * @param player The player
     */
    @Override
    protected void removeFromBar(@NotNull Player player) {
        BossBar bossBar = playerBars.remove(player);
        if (bossBar == null) return;
        player.hideBossBar(bossBar);
    }

    /**
     * Function to generate the progress of a player's bar
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    @FunctionalInterface
    interface ProgressFunction {
        /**
         * Get the progress for a given player
         *
         * @param player The player
         * @return The progress
         */
        @Range(from = 0, to = 1)
        float getProgress(@NotNull Player player);
    }

    /**
     * Builder class for Bars
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static class Builder extends Bar.Builder<Builder> {

        private MessageTemplate titleTemplate = MessageTemplate.of("");
        private ProgressFunction progressFunction = (player) -> 0;

        /**
         * Constructor
         */
        private Builder() {
        }

        /**
         * Get the title function.
         *
         * @return The title function.
         */
        @NotNull
        public MessageTemplate titleTemplate() {
            return titleTemplate;
        }

        /**
         * Set the title function
         *
         * @param titleTemplate The new title function
         * @return The builder
         */
        public Builder titleTemplate(@NotNull MessageTemplate titleTemplate) {
            this.titleTemplate = titleTemplate;
            return this;
        }

        /**
         * Get the progress function
         *
         * @return The progress function
         */
        @NotNull
        public ProgressFunction progressFunction() {
            return progressFunction;
        }

        /**
         * Set the progress function
         *
         * @param progressFunction The new progress function
         * @return The builder.
         */
        public Builder progressFunction(@NotNull ProgressFunction progressFunction) {
            this.progressFunction = progressFunction;
            return this;
        }

        /**
         * Build the bar
         *
         * @return The new bar
         */
        @Override
        public PerPlayerBar build() {
            return new PerPlayerBar(settings(), titleTemplate, progressFunction);
        }
    }
}