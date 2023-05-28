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

