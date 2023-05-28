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

import java.util.*;

/**
 * Interface to represent a {@see BossBar}
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public abstract class Bar {

    private final Collection<Player> activePlayers = new HashSet<>();
    private BarSettings settings;

    /**
     * Constructor.
     *
     * @param settings Settings for the BossBar
     */
    protected Bar(BarSettings settings) {
        this.settings = settings;
    }

    /**
     * Updates the bar for every player
     */
    public abstract void update();

    /**
     * Adds a single player to the bar
     *
     * @param player The player
     */
    public void addPlayer(@NotNull Player player) {
        if (!activePlayers.add(player)) return;
        addToBar(player);
    }

    /**
     * Adds a collection of players to the bar
     *
     * @param players The
     */
    public void addPlayers(@NotNull Collection<Player> players) {
        players.forEach(this::addPlayer);
    }

    public void removePlayer(@NotNull Player player) {
        if (!activePlayers.remove(player)) return;
        removeFromBar(player);
    }

    /**
     * Removes a collection of players from the bar.
     *
     * @param players The players.
     */
    public void removePlayers(@NotNull Collection<Player> players) {
        players.forEach(this::removePlayer);
    }

    /**
     * Get all players currently viewing this bar
     *
     * @return Collection of players.
     */
    @NotNull
    public Collection<Player> getActivePlayers() {
        return Collections.unmodifiableCollection(activePlayers);
    }

    /**
     * Get the current settings for the bar
     *
     * @return The settings
     */
    public BarSettings settings() {
        return settings;
    }

    /**
     * Set the settings for the bar
     *
     * @param settings The new settings
     */
    public void settings(@NotNull BarSettings settings) {
        this.settings = settings;
        update();
    }

    /**
     * Add show a new player the bar
     *
     * @param player The player
     */
    protected abstract void addToBar(@NotNull Player player);

    /**
     * Remove a player from the bar
     *
     * @param player The player
     */
    protected abstract void removeFromBar(@NotNull Player player);

    /**
     * Helper method to update the bar's properties.
     *
     * @param bar   The bar.
     * @param title The title.
     */
    protected void setBarProperties(@NotNull BossBar bar, @NotNull Component title, float progress) {
        bar.name(title)
                .progress(progress)
                .color(settings.color())
                .flags(settings.flags())
                .overlay(settings.overlay());
    }

    /**
     * Create a BossBar
     *
     * @param title    The title
     * @param progress The progress
     * @return The new BossBar
     */
    protected BossBar createBossBar(@NotNull Component title, float progress) {
        BossBar.Color color = settings.color();
        Set<BossBar.Flag> flags = settings.flags();
        BossBar.Overlay style = settings.overlay();

        return BossBar.bossBar(title, progress, color, style, flags);
    }

    /**
     * Builder class for Bars
     *
     * @param <T> Type of the builder
     * @author Skulduggerry
     * @since 0.1.0
     */
    public abstract static class Builder<T extends Builder<T>> {

        private BarSettings settings = BarSettings.builder().build();

        /**
         * Constructor
         */
        protected Builder() {
        }

        /**
         * Get the settings
         *
         * @return The settings
         */
        public BarSettings settings() {
            return settings;
        }

        /**
         * Set new settings
         *
         * @param settings The new settings
         * @return The builder
         */
        @SuppressWarnings("unchecked")
        public T settings(@NotNull BarSettings settings) {
            this.settings = settings;
            return (T) this;
        }

        /**
         * Build the bar
         *
         * @return The new bar
         */
        public abstract Bar build();
    }
}