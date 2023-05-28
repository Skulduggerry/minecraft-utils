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

import java.util.Objects;

/**
 * Implementation of the {@link AbstractPage} class.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class BoxPage extends AbstractPage {

    /**
     * Constructor.
     *
     * @param pageNumber   The number of the page.
     * @param template     The {@link PageTitleTemplate} of the page.
     * @param closeHandler The {@link CloseHandler} of the page.
     * @param type         The type of the page.
     */
    private BoxPage(@Range(from = 1, to = Integer.MAX_VALUE) int pageNumber, @NotNull PageTitleTemplate template, @Nullable CloseHandler closeHandler, @NotNull InventoryType type) {
        super(pageNumber, template, closeHandler, type);
    }

    /**
     * Static method to get a {@link Builder} object.
     * The type of the page must be a box, e.g. {@link InventoryType#DROPPER}.
     *
     * @param type the type of the page.
     * @return The builder.
     */
    public static Builder builder(@NotNull InventoryType type) {
        Objects.requireNonNull(type);
        return switch (type) {
            case DISPENSER, DROPPER, WORKBENCH -> new Builder(type);
            default -> throw new IllegalArgumentException("InventoryType must be a 3x3 inventory");
        };
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
    public Slot getSlot(@Range(from = 0, to = 2) int row, @Range(from = 0, to = 3) int column) {
        return slots[row * 3 + column];
    }

    /**
     * Implementation of the {@link AbstractPage} class.
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static final class Builder extends AbstractPage.Builder<Builder> {

        private final InventoryType type;

        /**
         * Constructor.
         *
         * @param type The type of the page.
         */
        private Builder(@NotNull InventoryType type) {
            super(type.defaultTitle());
            this.type = type;
        }

        /**
         * Get the size of the pages which will be created.
         *
         * @return the size.
         */
        @Override
        @Range(from = 1, to = 10)
        public int size() {
            return type.getDefaultSize();
        }

        /**
         * Build the page with the given attributes.
         *
         * @param pageNumber The number of the page (important for the {@link PageTitleTemplate}).
         * @return The new page.
         */
        @Override
        public Page build(@Range(from = 1, to = Integer.MAX_VALUE) int pageNumber) {
            return new BoxPage(pageNumber, title(), closeHandler(), type);
        }
    }
}
