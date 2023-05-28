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

package me.skulduggerry.utilities.menu.slot;

import me.skulduggerry.utilities.menu.ClickInformation;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Optional;

/**
 * Interface which represents a slot with a given {@link ItemTemplate} to create the items and a {@link ClickHandler} to handle clicks on this slot.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public interface Slot {

    /**
     * Get the index of the slot.
     *
     * @return The index.
     */
    @Range(from = 0, to = Integer.MAX_VALUE)
    int getIndex();

    /**
     * Get the item of the slot for the given player.
     *
     * @param player the player.
     * @return The item.
     */
    ItemStack getItem(@NotNull Player player);

    /**
     * Set the template for the items.
     *
     * @param template The template.
     */
    void setItemTemplate(@Nullable ItemTemplate template);

    /**
     * Checks weather the slot is empty.
     * A slot is empty when no {@link ItemTemplate} is set.
     *
     * @return The result of the check.
     */
    boolean isEmpty();

    /**
     * Get the click handler wrapped in an {@link Optional} because it can be null.
     *
     * @return The wrapped click handler.
     */
    @NotNull
    Optional<ClickHandler> getClickHandler();

    /**
     * Set the click handler.
     *
     * @param clickHandler The new click handler.
     */
    void setClickHandler(@Nullable ClickHandler clickHandler);

    /**
     * Set slot a {@link SlotSettings} to the slot.
     * Sets the {@link ItemTemplate} and the {@link ClickHandler}.
     *
     * @param settings The setting.
     */
    void setSlotSettings(@NotNull SlotSettings settings);

    /**
     * Updates the current slot
     */
    void update();

    /**
     * Updates the current slot
     *
     * @param player The player to update the slot for
     */
    void update(@NotNull Player player);

    /**
     * Interface which is used to handle clicks.
     *
     * @author Skulduggerry
     * @since 0.1.0
     */
    @FunctionalInterface
    interface ClickHandler {

        /**
         * Handles a click to a slot.
         *
         * @param information the information about the click.
         */
        void handleClick(@NotNull ClickInformation information);

        /**
         * Calls this handler after the given other handler
         *
         * @param other The other handler
         * @return The new handler
         */
        default ClickHandler after(@NotNull ClickHandler other) {
            return information -> {
                other.handleClick(information);
                handleClick(information);
            };
        }

        /**
         * Calls this handler before the given other handler
         *
         * @param other The other handler
         * @return Then new handler
         */
        default ClickHandler then(@NotNull ClickHandler other) {
            return information -> {
                handleClick(information);
                other.handleClick(information);
            };
        }
    }
}
