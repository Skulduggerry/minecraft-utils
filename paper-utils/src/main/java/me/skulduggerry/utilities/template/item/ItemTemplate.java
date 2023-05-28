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

import java.util.Objects;

/**
 * Interface which represents the template for an {@link ItemStack}.
 * Allows different items for every player.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
@FunctionalInterface
public interface ItemTemplate {

    /**
     * Get an item template which gives the same item for every player.
     *
     * @param material The material.
     * @return The template.
     */
    @NotNull
    static ItemTemplate of(@NotNull Material material) {
        return of(new ItemStack(material));
    }

    /**
     * Get an item template which gives the same item for every player.
     *
     * @param item The material.
     * @return The template.
     */
    @NotNull
    static ItemTemplate of(@NotNull ItemStack item) {
        Objects.requireNonNull(item);
        return player -> item;
    }

    /**
     * Makes this template to a template which allows to navigate in {@link MultiPageMenu}
     *
     * @return the new template
     */
    default MenuNavigateItemTemplate toMenuNavigationItem() {
        return (player, currentPage, totalPages) -> getItem(player);
    }

    /**
     * Get the item for the given player.
     * Every player can get a unique item.
     *
     * @param player The player.
     * @return The item.
     */
    @NotNull
    ItemStack getItem(@NotNull Player player);
}
