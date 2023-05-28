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

import me.skulduggerry.utilities.menu.mask.Mask;
import me.skulduggerry.utilities.menu.mask.AbstractMask;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Implementation of the mask which allows to add items to a page, e.g. like a frame around the page.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class SimpleMask extends AbstractMask {

    /**
     * Constructor.
     *
     * @param items The items.
     */
    private SimpleMask(ItemTemplate @NotNull [] @NotNull [] items) {
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
    public static final class Builder extends AbstractMask.Builder<Builder> {

        /**
         * Constructor.
         *
         * @param height Te height of the page.
         */
        private Builder(@Range(from = 1, to = 6) int height) {
            super(height);
        }

        /**
         * Builds the mask.
         *
         * @return The mask.
         */
        @NotNull
        @Override
        public Mask build() {
            return new SimpleMask(items);
        }
    }
}
