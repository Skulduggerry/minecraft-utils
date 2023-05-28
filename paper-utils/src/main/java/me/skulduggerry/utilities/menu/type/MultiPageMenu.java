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

package me.skulduggerry.utilities.menu.type;

import me.skulduggerry.utilities.menu.AbstractMenu;
import me.skulduggerry.utilities.menu.Menu;
import me.skulduggerry.utilities.menu.mask.Mask;
import me.skulduggerry.utilities.menu.page.Page;
import me.skulduggerry.utilities.menu.slot.SlotSettings;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import me.skulduggerry.utilities.template.item.MenuNavigateItemTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Implementation of the {@link AbstractMenu} class.
 * Realises a menu which is automatically growing and has multiple pages.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class MultiPageMenu extends AbstractMenu {

    private final int previousPageSlot;
    private final MenuNavigateItemTemplate previousPageItem;
    private final int nextPageSlot;
    private final MenuNavigateItemTemplate nextPageItem;

    /**
     * Constructor.
     *
     * @param pageBuilder      The builder  for pages.
     * @param parent           The parent menu.
     * @param mask             The mask.
     * @param closePageSlot    The slot for the close item.
     * @param closePageItem    The template for the close item.
     * @param parentMenuSlot   The slot for the parent menu.
     * @param parentMenuItem   The template for the parent menu item.
     * @param previousPageSlot The slot for the previous page item.
     * @param previousPageItem The template for the previous page item.
     * @param nextPageSlot     The slot for the next page item.
     * @param nextPageItem     The template for the next page item.
     */
    private MultiPageMenu(Page.@NotNull Builder<?> pageBuilder, @Nullable Menu parent,
                          @Nullable Mask mask,
                          int closePageSlot, ItemTemplate closePageItem,
                          int parentMenuSlot, ItemTemplate parentMenuItem,
                          int previousPageSlot, MenuNavigateItemTemplate previousPageItem,
                          int nextPageSlot, MenuNavigateItemTemplate nextPageItem) {
        super(pageBuilder, parent, mask, closePageSlot, closePageItem, parentMenuSlot, parentMenuItem);

        this.previousPageSlot = previousPageSlot;
        this.previousPageItem = previousPageItem;
        this.nextPageSlot = nextPageSlot;
        this.nextPageItem = nextPageItem;
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
     * Adds the settings for an empty slot.
     * If the menu is full and there is no next page slot because
     * If the menu is full, this method will automatically create a new page and try to add the item to this new page.
     * If this does not work the method will throw a {@link IllegalStateException} because the pages are always full.
     *
     * @param item The setting for the slot.
     * @return True if operation succeed.
     */
    @Override
    public boolean addItem(@NotNull SlotSettings item) {
        if (super.addItem(item)) return true;

        if (0 > nextPageSlot || nextPageItem == null) {
            return false;
        }

        Page page = createAndAddPage();
        int firstEmpty = page.getFirstEmpty();
        if (firstEmpty < 0)
            throw new IllegalStateException("Pages are always full. Please check the given mask.");
        page.getSlot(firstEmpty).setSlotSettings(item);
        return true;
    }

    /**
     * Applies the menu items to the new created page.
     * Also applies the mask to the menu if it is not null.
     * If a previous page exists like in {@link MultiPageMenu} this page will be also changed.
     *
     * @param current The new created page.
     */
    @Override
    protected void applyItems(@NotNull Page current) {
        super.applyItems(current);
        int totalPages = getPages().size();

        if (0 <= nextPageSlot && nextPageSlot < pageBuilder.size()) {
            setMenuItem(current, nextPageSlot, nextPageItem.toItemTemplate(totalPages, totalPages), information -> getPages().get(0).open(information.player()));
            if (totalPages > 1)
                getPages().get(totalPages - 2).getSlot(nextPageSlot).setClickHandler(information -> current.open(information.player()));
            for (int currentPageNumber = 0; currentPageNumber < totalPages - 1; ) {
                getPages().get(currentPageNumber++).getSlot(nextPageSlot).setItemTemplate(nextPageItem.toItemTemplate(currentPageNumber, totalPages));
            }
        }

        if (0 <= previousPageSlot && previousPageSlot < pageBuilder.size()) {
            setMenuItem(current, previousPageSlot, previousPageItem.toItemTemplate(totalPages, totalPages), information -> getPages().get(totalPages - 1).open(information.player()));
            for (int currentPageNumber = 0; currentPageNumber < totalPages - 1; ) {
                getPages().get(currentPageNumber++).getSlot(previousPageSlot).setItemTemplate(previousPageItem.toItemTemplate(currentPageNumber, totalPages));
            }
        }
    }

    /**
     * Implementation of the {@link AbstractMenu.Builder} class.
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static class Builder extends AbstractMenu.Builder<Builder> {

        private int previousPageSlot = -1;
        private MenuNavigateItemTemplate previousPageItem;
        private int nextPageSlot = -1;
        private MenuNavigateItemTemplate nextPageItem;

        /**
         * Constructor.
         *
         * @param pageBuilder The builder for pages.
         */
        private Builder(@NotNull Page.Builder<?> pageBuilder) {
            super(pageBuilder);
        }

        /**
         * Set the item which allows to go to the previous page of the menu.
         * Throws exception if slot is out of bound.
         *
         * @param previousPageSlot The slot for the item.
         * @param previousPageItem The template for the item.
         * @return The builder.
         */
        public Builder previousPage(@Range(from = 0, to = Integer.MAX_VALUE) int previousPageSlot, @NotNull ItemTemplate previousPageItem) {
            return previousPage(previousPageSlot, previousPageItem.toMenuNavigationItem());
        }

        /**
         * Set the item which allows to go to the previous page of the menu.
         * Throws exception if slot is out of bound.
         *
         * @param previousPageSlot The slot for the item.
         * @param previousPageItem The template for the item.
         * @return The builder.
         */
        public Builder previousPage(@Range(from = 0, to = Integer.MAX_VALUE) int previousPageSlot, @NotNull MenuNavigateItemTemplate previousPageItem) {
            if (pageBuilder().size() >= previousPageSlot) {
                throw new IllegalArgumentException("Try to use slot with index %s but page's max index is %s".formatted(previousPageItem, pageBuilder().size() - 1));
            }
            this.previousPageSlot = previousPageSlot;
            this.previousPageItem = previousPageItem;
            return this;
        }

        /**
         * Get the slot for the previous page item.
         *
         * @return The slot.
         */
        public int previousPageSlot() {
            return previousPageSlot;
        }

        /**
         * Get the template for the previous page item.
         *
         * @return The template.
         */
        public MenuNavigateItemTemplate previousPageItem() {
            return previousPageItem;
        }

        /**
         * Set the item which allows to go to the next page.
         * Throws exception if slot is out of bound.
         *
         * @param nextPageSlot The slot for the item.
         * @param nextPageItem The template for the item if there is a new page.
         * @return The builder.
         */
        public Builder nextPage(@Range(from = 0, to = Integer.MAX_VALUE) int nextPageSlot, @NotNull ItemTemplate nextPageItem) {
            return nextPage(nextPageSlot, nextPageItem.toMenuNavigationItem());
        }

        /**
         * Set the item which allows to go to the next page.
         * Throws exception if slot is out of bound.
         *
         * @param nextPageSlot The slot for the item.
         * @param nextPageItem The template for the item if there is a new page.
         * @return The builder.
         */
        public Builder nextPage(@Range(from = 0, to = Integer.MAX_VALUE) int nextPageSlot, @NotNull MenuNavigateItemTemplate nextPageItem) {
            if (pageBuilder().size() >= nextPageSlot) {
                throw new IllegalArgumentException("Try to use slot with index %s but page's max index is %s".formatted(nextPageSlot, pageBuilder().size() - 1));
            }

            this.nextPageSlot = nextPageSlot;
            this.nextPageItem = nextPageItem;
            return this;
        }

        /**
         * Get the slot of the next page item.
         *
         * @return The slot.
         */
        public int nextPageSlot() {
            return nextPageSlot;
        }

        /**
         * Get the template of the next page item if there is a next page.
         *
         * @return the template.
         */
        public MenuNavigateItemTemplate nextPageItem() {
            return nextPageItem;
        }

        /**
         * Builds the menu.
         *
         * @return The menu.
         */
        @Override
        public @NotNull Menu build() {
            return new MultiPageMenu(pageBuilder(), parent(),
                    mask(),
                    closePageSlot(), closePageItem(),
                    parentMenuSlot(), parentMenuItem(),
                    previousPageSlot(), previousPageItem(),
                    nextPageSlot(), nextPageItem());
        }
    }
}
