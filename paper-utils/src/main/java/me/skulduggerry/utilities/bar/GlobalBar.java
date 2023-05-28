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

