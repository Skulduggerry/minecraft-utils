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

package me.skulduggerry.utilities.menu.type;

import me.skulduggerry.utilities.menu.AbstractMenu;
import me.skulduggerry.utilities.menu.Menu;
import me.skulduggerry.utilities.menu.mask.Mask;
import me.skulduggerry.utilities.menu.page.Page;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of the {@link AbstractMenu} class.
 * Realises a menu with only one page.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class SimpleMenu extends AbstractMenu {

    /**
     * Constructor.
     *
     * @param pageBuilder    The builder for pages.
     * @param parent         The parent menu.
     * @param mask           The mask.
     * @param closePageSlot  The slot for the close item.
     * @param closePageItem  The template for the close item.
     * @param parentMenuSlot The slot for the parent menu item.
     * @param parentMenuItem The template for the parent menu item.
     */
    private SimpleMenu(Page.@NotNull Builder<?> pageBuilder, @Nullable Menu parent,
                       @Nullable Mask mask,
                       int closePageSlot, ItemTemplate closePageItem,
                       int parentMenuSlot, ItemTemplate parentMenuItem) {
        super(pageBuilder, parent, mask, closePageSlot, closePageItem, parentMenuSlot, parentMenuItem);
    }

    /**
     * Static method to get a builder object.
     *
     * @param pageBuilder The builder for pages.
     * @return The builder object.
     */
    public static Builder builder(Page.@NotNull Builder<?> pageBuilder) {
        return new Builder(pageBuilder);
    }

    /**
     * Implementation of the {@link AbstractMenu.Builder} class.
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static class Builder extends AbstractMenu.Builder<Builder> {

        /**
         * Constructor.
         *
         * @param pageBuilder The builder for pages.
         */
        private Builder(@NotNull Page.Builder<?> pageBuilder) {
            super(pageBuilder);
        }

        /**
         * Builds the menu.
         *
         * @return The menu.
         */
        @Override
        @NotNull
        public Menu build() {
            return new SimpleMenu(pageBuilder(), parent(),
                    mask(),
                    closePageSlot(), closePageItem(),
                    parentMenuSlot(), parentMenuItem());
        }
    }
}
