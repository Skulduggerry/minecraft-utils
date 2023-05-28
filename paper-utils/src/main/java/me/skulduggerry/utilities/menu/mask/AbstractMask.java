/*
 * MIT License
 *
 * Copyright (c) 2022 Skulduggerry
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
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

package me.skulduggerry.utilities.menu.mask;

import me.skulduggerry.utilities.menu.page.Page;
import me.skulduggerry.utilities.menu.slot.SlotSettings;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Abstract class which allows to add items to a page, e.g. like a frame around the page.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public abstract class AbstractMask implements Mask {

    private final ItemTemplate[][] items;

    /**
     * Constructor.
     *
     * @param items The inventory.
     */
    protected AbstractMask(ItemTemplate[][] items) {
        this.items = items;
    }

    /**
     * Applies the mask on a page.
     *
     * @param page The page.
     */
    @Override
    public void applyMaskOn(@NonNull Page page) {
        for (int row = 0; row < items.length && row < page.getHeight(); ++row) {
            for (int column = 0; column < items[row].length && column < page.getWidth(); ++column) {
                SlotSettings settings = SlotSettings.builder()
                        .handler(information -> information.event().setCancelled(true))
                        .template(items[row][column])
                        .build();
                page.getSlot(row, column).setSlotSettings(settings);
            }
        }
    }

    /**
     * Class which allows the easy creation of masks using the builder pattern.
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static abstract class Builder<T extends Builder<T>> implements Mask.Builder<T> {

        protected final ItemTemplate[][] items;
        private int rowPointer = 0;

        /**
         * Constructor.
         *
         * @param height The height of the page.
         */
        protected Builder(@Positive int height) {
            this.items = new ItemTemplate[height][];
        }

        /**
         * Get the current row.
         *
         * @return Current row.
         */
        protected int getRow() {
            return rowPointer;
        }

        /**
         * Jump to first row.
         *
         * @return The builder.
         */
        @SuppressWarnings("unchecked")
        @Override
        public T firstRow() {
            this.rowPointer = 0;
            return (T) this;
        }

        /**
         * Jump to last row.
         *
         * @return The builder.
         */
        @SuppressWarnings("unchecked")
        @Override
        public T lastRow() {
            this.rowPointer = items.length - 1;
            return (T) this;
        }

        /**
         * Goes to next row.
         *
         * @return The builder.
         */
        @SuppressWarnings("unchecked")
        @Override
        public T nextRow() {
            if (this.rowPointer < items.length - 1) ++this.rowPointer;
            return (T) this;
        }

        /**
         * Goes to previous row if it exists.
         * Otherwise, it does nothing.
         *
         * @return The builder
         */
        @SuppressWarnings("unchecked")
        @Override
        public T previousRow() {
            if (rowPointer > 0) --rowPointer;
            return (T) this;
        }

        /**
         * Set the row.
         * Automatically goes to next row.
         *
         * @param items The items in the row.
         * @return The builder
         */
        @SuppressWarnings("unchecked")
        @Override
        public T set( ItemTemplate @NonNull [] items) {
            this.items[rowPointer] = items;
            nextRow();
            return (T) this;
        }
    }
}
