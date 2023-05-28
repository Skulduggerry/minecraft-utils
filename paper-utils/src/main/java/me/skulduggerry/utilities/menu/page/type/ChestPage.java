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

package me.skulduggerry.utilities.menu.page.type;

import me.skulduggerry.utilities.menu.page.Page;
import me.skulduggerry.utilities.menu.slot.Slot;
import me.skulduggerry.utilities.menu.page.AbstractPage;
import me.skulduggerry.utilities.template.title.PageTitleTemplate;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Implementation of the {@link AbstractPage} class.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class ChestPage extends AbstractPage {

    /**
     * Constructor.
     *
     * @param pageNumber   The number of the page.
     * @param template     The {@link PageTitleTemplate} of the page.
     * @param closeHandler The {@link CloseHandler} of the page.
     * @param height       The number height of this page.
     */
    private ChestPage(@Range(from = 1, to = Integer.MAX_VALUE) int pageNumber,
                      @NotNull PageTitleTemplate template,
                      @Nullable CloseHandler closeHandler,
                      @Range(from = 1, to = 6) int height) {
        super(pageNumber, template, closeHandler, 9, height);
    }

    /**
     * Static method to get a {@link Builder} object.
     *
     * @param rows The number of rows this page should have.
     * @return The builder.
     */
    public static Builder builder(@Range(from = 1, to = 6) int rows) {
        return new Builder(rows);
    }

    /**
     * Get the slot at the given row and column.
     * If the calculated does not exist a {@link ArrayIndexOutOfBoundsException} will be thrown.
     *
     * @param row    The row of the page.
     * @param column The column of the page.
     * @return The slot at the given index.
     */
    @Override
    public Slot getSlot(@Range(from = 0, to = 6) int row, @Range(from = 0, to = 8) int column) {
        return slots[row * 9 + column];
    }

    /**
     * Implementation of the {@link AbstractPage} class.
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static final class Builder extends AbstractPage.Builder<Builder> {

        private final int height;

        /**
         * Constructor.
         *
         * @param rows The number of rows this builder should have.
         */
        private Builder(@Range(from = 9, to = 54) int rows) {
            super(InventoryType.CHEST.defaultTitle());
            this.height = rows * 9;
        }

        /**
         * Get the size of the pages which will be created.
         *
         * @return the size.
         */
        @Override
        @Range(from = 9, to = 54)
        public int size() {
            return height * 9;
        }

        /**
         * Build the page with the given attributes.
         *
         * @param pageNumber The number of the page (important for the {@link PageTitleTemplate}).
         * @return The new page.
         */
        @Override
        public Page build(@Range(from = 1, to = Integer.MAX_VALUE) int pageNumber) {
            return new ChestPage(pageNumber, title(), closeHandler(), height);
        }
    }
}
