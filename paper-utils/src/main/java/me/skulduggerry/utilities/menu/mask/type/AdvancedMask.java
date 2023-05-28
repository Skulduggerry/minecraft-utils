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

import me.skulduggerry.utilities.menu.mask.AbstractMask;
import me.skulduggerry.utilities.menu.mask.Mask;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the mask which allows to add items to a page, e.g. like a frame around the page.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class AdvancedMask extends AbstractMask {

    /**
     * Constructor.
     *
     * @param items The items.
     */
    private AdvancedMask(ItemTemplate @NotNull [] @NotNull [] items) {
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
     * Not that items which are set with {@link Mask.Builder#set(ItemTemplate[])} will be overridden
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static class Builder extends AbstractMask.Builder<Builder> {

        private final String[] shape;
        private final Map<Character, ItemTemplate> parameters;

        /**
         * Constructor.
         *
         * @param height The height of the page.
         */
        private Builder(@Range(from = 1, to = 6) int height) {
            super(height);
            this.shape = new String[height];
            Arrays.fill(shape, "");
            this.parameters = new HashMap<>();
        }

        /**
         * Set the pattern for the current row.
         *
         * @param row The pattern.
         * @return The builder.
         */
        public Builder set(@NotNull String row) {
            shape[getRow()] = row;
            nextRow();
            return this;
        }

        /**
         * Give the item for a key.
         * Automatically goes to next row.
         *
         * @param key   The key in the pattern.
         * @param value The item defined for the key.
         * @return The builder.
         */
        public Builder setItemParameter(char key, ItemTemplate value) {
            parameters.put(key, value);
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
            firstRow();
            for (String row : shape) {
                ItemTemplate[] itemsInRow = new ItemTemplate[row.length()];
                for (int column = 0; column < itemsInRow.length; ++column) {
                    char key = row.charAt(column);
                    itemsInRow[column] = parameters.get(key);
                }
                set(itemsInRow);
            }

            return new AdvancedMask(items);
        }
    }
}
