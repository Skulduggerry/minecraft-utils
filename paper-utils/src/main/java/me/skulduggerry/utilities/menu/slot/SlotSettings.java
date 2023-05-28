/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Skulduggerry
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

package me.skulduggerry.utilities.menu.slot;

import me.skulduggerry.utilities.template.item.ItemTemplate;
import org.jetbrains.annotations.Nullable;

/**
 * Data class which is used to bundle a {@link ItemTemplate} and a {@link Slot.ClickHandler}.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class SlotSettings {

    private final @Nullable ItemTemplate itemTemplate;
    private final Slot.@Nullable ClickHandler clickHandler;

    /**
     * Constructor.
     *
     * @param itemTemplate The item template.
     * @param clickHandler The click handler.
     */
    private SlotSettings(@Nullable ItemTemplate itemTemplate, Slot.@Nullable ClickHandler clickHandler) {
        this.itemTemplate = itemTemplate;
        this.clickHandler = clickHandler;
    }

    /**
     * Static class to which is used  to get a builder object.
     *
     * @return The builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get the item template.
     *
     * @return The item template.
     */
    @Nullable
    public ItemTemplate getItemTemplate() {
        return itemTemplate;
    }

    /**
     * Get the click handler.
     *
     * @return The click handler.
     */
    public Slot.@Nullable ClickHandler getClickHandler() {
        return clickHandler;
    }

    /**
     * Builder class for easy creation of slot settings.
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static class Builder {

        private @Nullable ItemTemplate itemTemplate;
        private Slot.@Nullable ClickHandler clickHandler;

        /**
         * Constructor.
         */
        private Builder() {
        }

        /**
         * Set the item template.
         *
         * @param itemTemplate The new item template.
         * @return The builder.
         */
        public Builder template(@Nullable ItemTemplate itemTemplate) {
            this.itemTemplate = itemTemplate;
            return this;
        }

        /**
         * Set the click handler.
         *
         * @param clickHandler The click handler.
         * @return The builder.
         */
        public Builder handler(Slot.@Nullable ClickHandler clickHandler) {
            this.clickHandler = clickHandler;
            return this;
        }

        /**
         * Builds the slot settings.
         *
         * @return The slot settings.
         */
        public SlotSettings build() {
            return new SlotSettings(itemTemplate, clickHandler);
        }
    }
}
