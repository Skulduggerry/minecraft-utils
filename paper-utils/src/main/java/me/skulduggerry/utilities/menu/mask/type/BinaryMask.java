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

package me.skulduggerry.utilities.menu.mask.type;

import me.skulduggerry.utilities.builder.ItemBuilder;
import me.skulduggerry.utilities.menu.mask.Mask;
import me.skulduggerry.utilities.menu.mask.AbstractMask;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.naming.OperationNotSupportedException;

/**
 * Implementation of the mask which allows to create a mask with only two different items.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class BinaryMask extends AbstractMask {

    /**
     * Constructor.
     *
     * @param items The items.
     */
    private BinaryMask(ItemTemplate @NotNull [] @NotNull [] items) {
        super(items);
    }

    /**
     * Get a new builder.
     *
     * @param height The height of the page.
     * @return The builder.
     */
    public static Builder builder(@Range(from = 1, to = 6) int height) {
        return new Builder(height);
    }

    /**
     * Class which allows the easy creation of masks using the builder pattern.
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static class Builder extends AbstractMask.Builder<Builder> {

        public static final ItemTemplate DEFAULT_ZERO = ItemTemplate.of(new ItemBuilder<>(Material.WHITE_STAINED_GLASS_PANE).setDisplayName(Component.empty()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build());

        public static final ItemTemplate DEFAULT_ONE = ItemTemplate.of(new ItemBuilder<>(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(Component.empty()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build());

        private ItemTemplate zero = DEFAULT_ZERO;
        private ItemTemplate one = DEFAULT_ONE;
        private final char[][] chars;

        /**
         * Constructor.
         *
         * @param height The height of the page.
         */
        private Builder(@Range(from = 1, to = 6) int height) {
            super(height);
            chars = new char[height][];
        }

        /**
         * Not supported in binary masks.
         *
         * @throws UnsupportedOperationException Binary masks builders does not support this method.
         */
        @Override
        public Builder set(ItemTemplate @NotNull [] items) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        /**
         * Set the row.
         * Automatically goes to next row.
         * Creates a row by interpreting 0s as {@link #zero} and 1s as {@link #one}.
         * All other symbols will be interpreted as no item.
         * <p>
         * As an example:
         * 01010101
         * Will add four times first a {@link #zero} and then a {@link #one}.
         *
         * @param row The row.
         * @return The builder
         */
        public Builder set(@NotNull String row) {
            chars[getRow()] = row.toCharArray();
            return this;
        }

        /**
         * Sets the new zero item
         *
         * @param zero The new item
         * @return The builder.
         */
        public Builder zero(@NotNull ItemTemplate zero) {
            this.zero = zero;
            return this;
        }

        /**
         * Sets the new one item
         *
         * @param one The new item.
         * @return The builder.
         */
        public Builder one(@NotNull ItemTemplate one) {
            this.one = one;
            return this;
        }

        /**
         * Builds the mask.
         *
         * @return The mask.
         */
        @NotNull
        @Override
        public Mask build() {
            for (int rowIdx = 0; rowIdx < chars.length; ++rowIdx) {
                ItemTemplate[] row = new ItemTemplate[chars[rowIdx].length];
                for (int columnIdx = 0; columnIdx < row.length; ++columnIdx) {
                    row[columnIdx] = switch (chars[rowIdx][columnIdx]) {
                        case '0' -> zero;
                        case '1' -> one;
                        default -> null;
                    };
                }
                this.items[rowIdx] = row;
            }
            return new BinaryMask(items);
        }
    }
}
