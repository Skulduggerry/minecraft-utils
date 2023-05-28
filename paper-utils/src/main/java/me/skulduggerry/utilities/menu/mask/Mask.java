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

package me.skulduggerry.utilities.menu.mask;

import me.skulduggerry.utilities.menu.page.Page;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import org.jetbrains.annotations.NotNull;


/**
 * Add overlays to pages.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
@FunctionalInterface
public interface Mask {

    /**
     * Applies the mask on the page.
     *
     * @param page The page.
     */
    void applyMaskOn(@NotNull Page page);

    /**
     * Class which allows the easy creation of masks using the builder pattern.
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    interface Builder<T extends Builder<T>> {

        /**
         * Jump to first row.
         *
         * @return The builder.
         */
        T firstRow();

        /**
         * Jump to last row.
         *
         * @return The builder.
         */
        T lastRow();

        /**
         * Goes to next row.
         *
         * @return The builder.
         */
        T nextRow();


        T previousRow();

        /**
         * Set the row.
         * Automatically goes to next row.
         *
         * @param items The items in the row.
         * @return The builder
         */
        T set(ItemTemplate @NotNull [] items);

        /**
         * Builds the mask.
         *
         * @return The mask.
         */
        @NotNull
        Mask build();
    }
}
