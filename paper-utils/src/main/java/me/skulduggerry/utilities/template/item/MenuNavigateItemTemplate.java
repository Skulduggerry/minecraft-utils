/*
 * MIT License
 *
 * Copyright (c) 2023 Skulduggerry
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

package me.skulduggerry.utilities.template.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * Interface which represents the template for an {@link ItemStack}.
 * Allows different items for every player.
 * It is designed to allow the navigation in menus with multiple pages made of inventories.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
@FunctionalInterface
public interface MenuNavigateItemTemplate {

    /**
     * Get an item template which gives the same item for every player.
     *
     * @param material The material.
     * @return The template.
     */
    @NotNull
    static MenuNavigateItemTemplate of(@NotNull Material material) {
        return of(new ItemStack(material));
    }

    /**
     * Get an item template which gives the same item for every player.
     *
     * @param item The material.
     * @return The template.
     */
    @NotNull
    static MenuNavigateItemTemplate of(@NotNull ItemStack item) {
        Objects.requireNonNull(item);
        return (player, currentPage, totalPages) -> item;
    }

    /**
     * Makes this to an item template which can be used to look different on every page
     *
     * @param currentPage page the navigation item will be placed on
     * @param totalPages  the total number of pages
     * @return the template.
     */
    @NotNull
    default ItemTemplate toItemTemplate(@Range(from = 1, to = Integer.MAX_VALUE) int currentPage, @Range(from = 1, to = Integer.MAX_VALUE) int totalPages) {
        return player -> getItem(player, currentPage, totalPages);
    }

    /**
     * Get the item for the given player.
     * Every player can get a unique item.
     *
     * @param player The player.
     * @return The item.
     */
    @NotNull
    ItemStack getItem(@NotNull Player player, @Range(from = 1, to = Integer.MAX_VALUE) int currentPage, @Range(from = 1, to = Integer.MAX_VALUE) int totalPages);
}

