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

package me.skulduggerry.utilities.menu.page;

import me.skulduggerry.utilities.exception.SlotIndexOutOfBoundsException;
import me.skulduggerry.utilities.menu.slot.Slot;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import me.skulduggerry.utilities.template.title.MenuPageTitleTemplate;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Collection;
import java.util.Optional;

/**
 * Interface which represents one page.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public interface Page {

    /**
     * Get the holders of pages.
     * Important to check whether an opened inventory is a page.
     *
     * @return A {@link Collection} of {@link PageHolder}s.
     */
    Collection<PageHolder> getHolders();

    /**
     * Get the player who are watching at this page.
     *
     * @return A {@link Collection} of {@link Player}s.
     */
    Collection<Player> getViewers();

    /**
     * Checks weather a player has opened this page.
     *
     * @param player The player.
     * @return The result of the check.
     */
    boolean isOpen(@NotNull Player player);

    /**
     * Opens the page for the player.
     *
     * @param player The player.
     */
    void open(@NotNull Player player);

    /**
     * Closes this page for all players.
     */
    void close();

    /**
     * Closes this page for the given player.
     *
     * @param player The player.
     */
    void close(@NotNull Player player);

    /**
     * Called when this page is closed by the player.
     *
     * @param player          The player.
     * @param activateTrigger Option to trigger the {@link #getCloseHandler()} if present.
     */
    void closedByPlayer(@NotNull Player player, boolean activateTrigger);

    /**
     * Updates a single slot.
     * If index is less 0 or index is more than the size of the page an {@link SlotIndexOutOfBoundsException} will be thrown.
     *
     * @param slot The slot to update.
     */
    void updateSlot(int slot);

    /**
     * Updates a single slot for the given player.
     * If index is less 0 or index is more than the size of the page an {@link SlotIndexOutOfBoundsException} will be thrown.
     *
     * @param slot   The slot to update.
     * @param player The player.
     */
    void updateSlot(int slot, @NotNull Player player);

    /**
     * Updates this page for all players.
     */
    void update();

    /**
     * Updates this page for the given player.
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
     * Get the slot at the given index.
     * If index is less 0 or index is more than the size of the page an {@link SlotIndexOutOfBoundsException} will be thrown.
     *
     * @param index The index.
     * @return The slot at the given index.
     */
    Slot getSlot(int index);

    /**
     * Get the slot at the given row and column.
     * If the calculated does not exist a {@link SlotIndexOutOfBoundsException} will be thrown.
     *
     * @param row    The row of the page.
     * @param column The column of the page.
     * @return The slot at the given index.
     */
    Slot getSlot(int row, int column);

    /**
     * Removes all items from the page.
     */
    void clear();

    /**
     * Clears the slot at the given index.
     * If index is less 0 or index is more than the size of the page an {@link SlotIndexOutOfBoundsException} will be thrown.
     *
     * @param index The index.
     */
    void clear(int index);

    /**
     * Get the size of the page.
     *
     * @return The size.
     */
    int getSize();

    /**
     * Get the width of this page.
     *
     * @return The width.
     */
    int getWidth();

    /**
     * Get the height of this page.
     *
     * @return The height.
     */
    int getHeight();

    /**
     * Get the index of the first empty slot or -1 of all slots are full.
     * A slot is empty when no {@link ItemTemplate} is set.
     *
     * @return The index.
     */
    int getFirstEmpty();

    /**
     * Get the {@link CloseHandler} of this page wrapped in a {@link Optional}.
     *
     * @return The wrapped {@link CloseHandler}.
     */
    Optional<CloseHandler> getCloseHandler();

    /**
     * Set the{@link CloseHandler} of this page.
     *
     * @param closeHandler the new {@link CloseHandler}.
     */
    void setCloseHandler(@Nullable CloseHandler closeHandler);

    /**
     * Functional interface which handles the close of a page.
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    @FunctionalInterface
    interface CloseHandler {

        /**
         * This method is called when this page is closed.
         *
         * @param player The player who closed the page.
         * @param page   The closed page.
         */
        void handleClose(@NotNull Player player, @NotNull Page page);
    }

    /**
     * Interface which represents a builder for pages.
     *
     * @param <T> The type of the builder.
     * @author Skulduggerry
     * @since 0.1.0
     */
    interface Builder<T extends Builder<T>> {

        /**
         * Set the {@link MenuPageTitleTemplate}.
         *
         * @param titleTemplate The new {@link MenuPageTitleTemplate}.
         * @return The builder.
         */
        T title(@NotNull MenuPageTitleTemplate titleTemplate);

        /**
         * Set a static title for all players.
         *
         * @param title The title.
         * @return the builder.
         */
        T title(@NotNull Component title);

        /**
         * Get the {@link MenuPageTitleTemplate} for this builder.
         *
         * @return The {@link MenuPageTitleTemplate}.
         */
        @NotNull
        MenuPageTitleTemplate title();

        /**
         * Set the {@link CloseHandler} for this builder.
         *
         * @param closeHandler The new {@link CloseHandler}.
         * @return the builder.
         */
        T closeHandler(@Nullable CloseHandler closeHandler);

        /**
         * Get the {@link CloseHandler}.
         *
         * @return The {@link CloseHandler}.
         */
        @Nullable
        CloseHandler closeHandler();

        /**
         * Get the size of the pages which will be created.
         *
         * @return the size.
         */
        @Range(from = 1, to = Integer.MAX_VALUE)
        int size();

        /**
         * Build the page with the given attributes.
         *
         * @param pageNumber The number of the page (important for the {@link MenuPageTitleTemplate}).
         * @return The new page.
         */
        Page build(@Range(from = 1, to = Integer.MAX_VALUE) int pageNumber);

        /**
         * Build the page with the given attributes and set the pageNumber automatically to one.
         *
         * @return The new page.
         */
        Page build();
    }
}
