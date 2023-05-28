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
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class to store a {@link Bar}'s looking
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class BarSettings {

    private final BossBar.Color color;
    private final Set<BossBar.Flag> flags;
    private final BossBar.Overlay overlay;

    /**
     * Constructor.
     *
     * @param color   The bar's color
     * @param flags   The bar's flags
     * @param overlay The bar's style
     */
    private BarSettings(@NotNull BossBar.Color color, @NotNull Set<BossBar.Flag> flags, @NotNull BossBar.Overlay overlay) {
        this.color = color;
        this.flags = flags;
        this.overlay = overlay;
    }

    /**
     * Get a new builder.
     *
     * @return The ew builder.
     */
    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get a new builder but saves all values of the given setting.
     *
     * @param settings The setting.
     * @return The new builder.
     */
    @NotNull
    public static Builder builder(@NotNull BarSettings settings) {
        return new Builder()
                .color(settings.color())
                .addFlags(settings.flags)
                .overlay(settings.overlay());
    }

    /**
     * Get the color
     *
     * @return The color
     */
    @NotNull
    public BossBar.Color color() {
        return color;
    }

    /**
     * Get the flags
     *
     * @return The flags
     */
    @NotNull
    public Set<BossBar.Flag> flags() {
        return Collections.unmodifiableSet(flags);
    }

    /**
     * Get the style
     *
     * @return The style
     */
    @NotNull
    public BossBar.Overlay overlay() {
        return overlay;
    }

    /**
     * Builder class for settings.
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static class Builder {

        private BossBar.Color color = BossBar.Color.WHITE;
        private final Set<BossBar.Flag> flags = new HashSet<>();
        private BossBar.Overlay overlay = BossBar.Overlay.PROGRESS;

        /**
         * Constructor.
         */
        private Builder() {
        }

        /**
         * Set the color
         *
         * @param color The new color
         * @return The builder.
         */
        public Builder color(@NotNull BossBar.Color color) {
            this.color = color;
            return this;
        }

        /**
         * Add the given flags
         *
         * @param flags The flags
         * @return The builder
         */
        public Builder addFlags(@NotNull Collection<BossBar.Flag> flags) {
            this.flags.addAll(flags);
            return this;
        }

        /**
         * Add the given flags
         *
         * @param flags The flags
         * @return The builder
         */
        public Builder addFlags(@NotNull BossBar.Flag @NotNull ... flags) {
            this.flags.addAll(Arrays.asList(flags));
            return this;
        }

        /**
         * Remove the given flags
         *
         * @param flags The flags
         * @return The builder.
         */
        public Builder removeFlags(@NotNull Collection<BossBar.Flag> flags) {
            this.flags.removeAll(flags);
            return this;
        }

        /**
         * Remove the given flags
         *
         * @param flags The flags
         * @return The builder.
         */
        public Builder removeFlags(BossBar.Flag... flags) {
            Arrays.asList(flags).forEach(this.flags::remove);
            return this;
        }

        /**
         * Set the style
         *
         * @param overlay The new style
         * @return The builder
         */
        public Builder overlay(@NotNull BossBar.Overlay overlay) {
            this.overlay = overlay;
            return this;
        }

        /**
         * Build the settings.
         *
         * @return The new settings.
         */
        public BarSettings build() {
            return new BarSettings(color, flags, overlay);
        }
    }
}

