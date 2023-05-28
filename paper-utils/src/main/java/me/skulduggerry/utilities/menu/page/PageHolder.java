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

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents the owner of a pages.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class PageHolder implements InventoryHolder {

    private final Player player;
    private Page page;
    private Inventory inventory;

    /**
     * Constructor.
     *
     * @param player The player which holds the page.
     * @param page   The page.
     */
    public PageHolder(@NotNull Player player, @NotNull Page page) {
        this.player = player;
        this.page = page;
    }

    /**
     * Get the player.
     *
     * @return The player.
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the page.
     *
     * @return The page.
     */
    @NotNull
    public Page getPage() {
        return page;
    }

    /**
     * Set the page.
     * Used when the player navigates to a new page.
     *
     * @param page The new page.
     */
    public void setPage(@NotNull Page page) {
        this.page = Objects.requireNonNull(page);
    }


    /**
     * Get the inventory.
     *
     * @return The inventory.
     */
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Set the inventory.
     * Used when the player navigates to a new page.
     *
     * @param inventory The new inventory.
     */
    public void setInventory(@NotNull Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public int hashCode() {
        return player.getName().hashCode();
    }
}