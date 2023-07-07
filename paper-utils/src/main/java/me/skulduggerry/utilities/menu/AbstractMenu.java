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

package me.skulduggerry.utilities.menu;

import me.skulduggerry.utilities.menu.mask.Mask;
import me.skulduggerry.utilities.menu.page.Page;
import me.skulduggerry.utilities.menu.slot.Slot;
import me.skulduggerry.utilities.menu.slot.SlotSettings;
import me.skulduggerry.utilities.menu.type.MultiPageMenu;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

/**
 * Abstraction of the {@link Menu} interface.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public abstract class AbstractMenu implements Menu {

    protected final Page.Builder<?> pageBuilder;
    protected final Menu parent;

    protected final Mask mask;

    protected final int closePageSlot;
    protected final ItemTemplate closePageItem;
    protected final int parentMenuSlot;
    protected final ItemTemplate parentMenuItem;

    protected final List<Page> pages;

    /**
     * Constructor.
     *
     * @param pageBuilder    The builder  for pages.
     * @param parent         The parent menu.
     * @param mask           The mask.
     * @param closePageSlot  The slot for the close item.
     * @param closePageItem  The template for the close item.
     * @param parentMenuSlot The slot for the parent menu.
     * @param parentMenuItem The template for the parent menu item.
     */
    protected AbstractMenu(Page.@NotNull Builder<?> pageBuilder, @Nullable Menu parent,
                           @Nullable Mask mask,
                           int closePageSlot, ItemTemplate closePageItem,
                           int parentMenuSlot, ItemTemplate parentMenuItem) {
        this.pageBuilder = Objects.requireNonNull(pageBuilder);
        this.parent = parent;

        this.mask = mask;

        this.closePageSlot = closePageSlot;
        this.closePageItem = closePageItem;
        this.parentMenuSlot = parentMenuSlot;
        this.parentMenuItem = parentMenuItem;

        pages = new LinkedList<>();
    }

    /**
     * Get the parent menu.
     * Parent menu can be null and is therefore wrapped in an {@link Optional}.
     *
     * @return The optional with the parent menu.
     */
    @Override
    public Optional<Menu> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Get the pages of this menu.
     *
     * @return An unmodifiable list of the pages.
     */
    @Override
    @NotNull
    public List<Page> getPages() {
        return Collections.unmodifiableList(pages);
    }

    /**
     * Checks weather the menu is open for the given player.
     * The menu is open for the player if a page of the menu is open for the player.
     *
     * @param player The player.
     * @return The result of the check.
     */
    @Override
    public boolean isOpen(@NotNull Player player) {
        for (Page page : pages) {
            if (page.isOpen(player))
                return true;
        }
        return false;
    }

    /**
     * Opens the first page for of the menu for the player.
     *
     * @param player The player.
     */
    @Override
    public void open(@NotNull Player player) {
        if (!pages.isEmpty()) pages.get(0).open(player);
        else createAndAddPage().open(player);
    }

    /**
     * Closes all pages for all players.
     */
    @Override
    public void close() {
        pages.forEach(Page::close);
    }

    /**
     * Closes all pages for the given player.
     *
     * @param player The player.
     */
    @Override
    public void close(@NotNull Player player) {
        pages.forEach(page -> page.close(player));
    }

    /**
     * Updates the content of all pages for all players.
     */
    @Override
    public void update() {
        pages.forEach(Page::update);
    }

    /**
     * Updates the content of all pages for the given player.
     *
     * @param player The player.
     */
    @Override
    public void update(@NotNull Player player) {
        pages.forEach(page -> page.update(player));
    }

    @Override
    public void updateFull() {
        pages.forEach(Page::updateFull);
    }

    @Override
    public void updateFull(@NotNull Player player) {
        pages.forEach(page -> page.updateFull(player));
    }

    /**
     * Get the number of pages this menu has.
     *
     * @return Returns the size.
     */
    @Range(from = 1, to = Integer.MAX_VALUE)
    @Override
    public int getNumberOfPages() {
        return pages.size();
    }

    /**
     * Set the {@link Page.CloseHandler} for all pages.
     *
     * @param closeHandler The {@link Page.CloseHandler}.
     */
    @Override
    public void setCloseHandler(Page.@Nullable CloseHandler closeHandler) {
        pageBuilder.closeHandler(closeHandler);
        pages.forEach(page -> page.setCloseHandler(closeHandler));
    }

    /**
     * Adds the settings for an empty slot.
     *
     * @param item The setting for the slot.
     * @return True if operation succeed
     */
    @Override
    public boolean addItem(@NotNull SlotSettings item) {
        Slot emptySlot = findFirstEmptySlot();
        if (emptySlot == null) return false;
        emptySlot.setSlotSettings(item);
        return true;
    }

    /**
     * Helper method to find the first empty slot.
     * If all pages are full this method returns null.
     *
     * @return The first empty slot or null.
     */
    protected Slot findFirstEmptySlot() {
        if (pages.isEmpty()) createAndAddPage();
        for (Page page : pages) {
            int firstEmpty = page.getFirstEmpty();
            if (firstEmpty >= 0) return page.getSlot(firstEmpty);
        }
        return null;
    }

    /**
     * Creates a page and adds it to the {@link AbstractMenu#pages} list.
     */
    protected Page createAndAddPage() {
        Page current = pageBuilder.build(getNumberOfPages() + 1);
        pages.add(current);
        applyItems(current);
        return current;
    }

    /**
     * Applies the menu items to the new created page.
     * Also applies the mask to the menu if it is not null.
     * If a previous page exists like in {@link MultiPageMenu} this page will be also changed.
     *
     * @param current The new created page.
     */
    protected void applyItems(@NotNull Page current) {
        if (mask != null)
            mask.applyMaskOn(current);

        if (0 <= closePageSlot)
            setMenuItem(current, closePageSlot, closePageItem, information -> current.close(information.player()));
        if (0 <= parentMenuSlot && parent != null)
            setMenuItem(current, parentMenuSlot, parentMenuItem, information -> parent.open(information.player()));
    }

    /**
     * Set a given {@link ItemTemplate} to a given slot at a given page.
     *
     * @param page         The page.
     * @param slotIndex    The slot.
     * @param template     The {@link ItemTemplate}.
     * @param clickHandler The {@link Slot.ClickHandler}.
     */
    protected final void setMenuItem(Page page, int slotIndex, ItemTemplate template, Slot.ClickHandler clickHandler) {
        page.getSlot(slotIndex).setSlotSettings(
                SlotSettings.builder()
                        .template(template)
                        .handler(clickHandler)
                        .build()
        );
    }

    /**
     * Abstraction of the {@link Menu.Builder} interface.
     *
     * @param <T> The type of the builder.
     * @author Skulduggerry
     * @since 0.1.0
     */
    protected abstract static class Builder<T extends Builder<T>> implements Menu.Builder<T> {

        private final Page.Builder<?> pageBuilder;
        private Menu parent;
        private Mask mask;

        private int closePageSlot = -1;
        private ItemTemplate closePageItem;
        private int parentMenuSlot = -1;
        private ItemTemplate parentMenuItem;

        /**
         * Constructor.
         *
         * @param pageBuilder The builder for the pages.
         */
        protected Builder(Page.@NotNull Builder<?> pageBuilder) {
            this.pageBuilder = Objects.requireNonNull(pageBuilder);
        }

        /**
         * Get the builder for pages.
         *
         * @return The builder for pages.
         */
        @NotNull
        @Override
        public Page.@NotNull Builder<?> pageBuilder() {
            return pageBuilder;
        }

        /**
         * Set the parent menu.
         * Set the item which allows to open the first page of the parent menu.
         * Throws exception if slot is out of bound.
         *
         * @param slot           The slot for the item.
         * @param parentMenuItem The template for the item.
         * @param menu           The parent menu.
         * @return The builder.
         */
        @Override
        @SuppressWarnings("unchecked")
        public T parent(@Nullable Menu menu, @Range(from = 0, to = Integer.MAX_VALUE) int slot, @NotNull ItemTemplate parentMenuItem) {
            if (menu == null) {
                parent = null;
                this.parentMenuSlot = -1;
                this.parentMenuItem = null;
                return (T) this;
            }

            this.parent = menu;
            if (pageBuilder().size() >= slot) {
                throw new IllegalArgumentException("Try to use slot with index %s but page's max index is %s".formatted(slot, pageBuilder().size() - 1));
            }
            this.parentMenuSlot = slot;
            this.parentMenuItem = parentMenuItem;
            return (T) this;
        }

        /**
         * Get the parent menu.
         *
         * @return The parent menu.
         */
        @Override
        public @Nullable Menu parent() {
            return parent;
        }

        /**
         * Set the item which closes the menu.
         * Throws exception if slot is out of bound.
         *
         * @param slot      The slot for the item.
         * @param closeItem A template for the item.
         * @return The builder.
         */
        @Override
        @SuppressWarnings("unchecked")
        public T closePageItem(@Range(from = 0, to = Integer.MAX_VALUE) int slot, @NotNull ItemTemplate closeItem) {
            if (pageBuilder().size() >= slot) {
                throw new IllegalArgumentException("Try to use slot with index %s but page's max index is %s".formatted(slot, pageBuilder().size() - 1));
            }
            this.closePageSlot = slot;
            this.closePageItem = closeItem;
            return (T) this;
        }

        /**
         * Get the slot for the close item.
         *
         * @return The slot.
         */
        @Override
        public int closePageSlot() {
            return closePageSlot;
        }

        /**
         * Get the {@link ItemTemplate} for the close item.
         *
         * @return The template.
         */
        @Override
        public ItemTemplate closePageItem() {
            return closePageItem;
        }

        /**
         * Get the slot for the parent menu item.
         *
         * @return The slot.
         */
        @Override
        public int parentMenuSlot() {
            return parentMenuSlot;
        }

        /**
         * Get the {@link ItemTemplate} for the parent menu item.
         *
         * @return The template.
         */
        @Override
        public ItemTemplate parentMenuItem() {
            return parentMenuItem;
        }

        /**
         * Set the mask for the menu.
         *
         * @param mask The mask.
         * @return The builder.
         */
        @Override
        @SuppressWarnings("unchecked")
        public T mask(@Nullable Mask mask) {
            this.mask = mask;
            return (T) this;
        }

        /**
         * Get the mask of the menu.
         *
         * @return The mask.
         */
        @Override
        public @Nullable Mask mask() {
            return mask;
        }
    }
}
