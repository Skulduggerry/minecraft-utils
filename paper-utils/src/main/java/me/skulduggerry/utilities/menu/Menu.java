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

package me.skulduggerry.utilities.menu;

import me.skulduggerry.utilities.menu.mask.Mask;
import me.skulduggerry.utilities.menu.page.Page;
import me.skulduggerry.utilities.menu.slot.SlotSettings;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.Optional;

/**
 * Interface which represents a menu.
 * Menus are used to add special items to {@link Page}s, e.g. items to close the page.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public interface Menu {

    /**
     * Get the parent menu.
     * Parent menu can be null and is therefore wrapped in an {@link Optional}.
     *
     * @return The optional with the parent menu.
     */
    Optional<Menu> getParent();

    /**
     * Get the pages of this menu.
     *
     * @return An unmodifiable list of the pages.
     */
    @NotNull List<Page> getPages();

    /**
     * Checks weather the menu is open for the given player.
     * The menu is open for the player if a page of the menu is open for the player.
     *
     * @param player The player.
     * @return The result of the check.
     */
    boolean isOpen(@NotNull Player player);

    /**
     * Opens the first page for of the menu for the player.
     *
     * @param player The player.
     */
    void open(@NotNull Player player);

    /**
     * Closes all pages for all players.
     */
    void close();

    /**
     * Closes all pages for the given player.
     *
     * @param player The player.
     */
    void close(@NotNull Player player);

    /**
     * Updates the content of all pages for all players.
     */
    void update();

    /**
     * Updates the content of all pages for the given player.
     *
     * @param player The player.
     */
    void update(@NotNull Player player);

    /**
     * Recreates the full page for all players.
     * Necessary e.g. when the title changes.
     */
    void recreate();

    /**
     * Recreates the full page for the given player.
     * Necessary e.g. when the title changes.
     *
     * @param player The player.
     */
    void recreate(@NotNull Player player);

    /**
     * Get the number of pages this menu has.
     *
     * @return Returns the size.
     */
    @Range(from = 1, to = Integer.MAX_VALUE) int getNumberOfPages();

    /**
     * Set the {@link Page.CloseHandler} for all pages.
     *
     * @param closeHandler The {@link Page.CloseHandler}.
     */
    void setCloseHandler(Page.@Nullable CloseHandler closeHandler);

    /**
     * Adds the settings for an empty slot.
     *
     * @param item The setting for the slot.
     * @return True if operation succeed
     */
    boolean addItem(@NotNull SlotSettings item);

    /**
     * Interface which represents a builder for menus.
     *
     * @param <T> The type of the builder.
     * @author Skulduggerry
     * @since 0.1.0
     */
    interface Builder<T extends Builder<T>> {

        /**
         * Get the builder for pages.
         *
         * @return The builder for pages.
         */
        Page.@NotNull Builder<?> pageBuilder();

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
        T parent(@NotNull Menu menu, @Range(from = 0, to = Integer.MAX_VALUE) int slot, @NotNull ItemTemplate parentMenuItem);

        /**
         * Get the parent menu.
         *
         * @return The parent menu.
         */
        @Nullable Menu parent();

        /**
         * Get the slot for the parent menu item.
         *
         * @return The slot.
         */
        int parentMenuSlot();

        /**
         * Get the {@link ItemTemplate} for the parent menu item.
         *
         * @return The template.
         */
        ItemTemplate parentMenuItem();

        /**
         * Set the item which closes the menu.
         * Throws exception if slot is out of bound.
         *
         * @param slot      The slot for the item.
         * @param closeItem A template for the item.
         * @return The builder.
         */
        T closePageItem(@Range(from = 0, to = Integer.MAX_VALUE) int slot, @NotNull ItemTemplate closeItem);

        /**
         * Get the slot for the close item.
         *
         * @return The slot.
         */
        int closePageSlot();

        /**
         * Get the {@link ItemTemplate} for the close item.
         *
         * @return The template.
         */
        ItemTemplate closePageItem();

        /**
         * Set the mask for the menu.
         *
         * @param mask The mask.
         * @return The builder.
         */
        T mask(@Nullable Mask mask);

        /**
         * Get the mask of the menu.
         *
         * @return The mask.
         */
        @Nullable Mask mask();

        /**
         * Builds the menu.
         *
         * @return The menu.
         */
        @NotNull
        Menu build();
    }
}
