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
