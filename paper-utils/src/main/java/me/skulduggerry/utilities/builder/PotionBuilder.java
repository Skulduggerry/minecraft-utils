package me.skulduggerry.utilities.builder;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Class which allows to build potions with custom textures.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class PotionBuilder extends ItemBuilder<PotionBuilder> {

    /**
     * A potion builder with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Potion</i>Builder is only designed to contain
     * <i>potions</i>. Do not use this class to encapsulate Materials which
     * are not potions.</b>
     *
     * @param material item material
     * @param amount   stack size
     */
    public PotionBuilder(@NotNull Material material, @Range(from = 1, to = 64) int amount) {
        super(checkMaterial(material), amount);
    }

    /**
     * A potion builder with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Potion</i>Builder is only designed to contain
     * <i>potions</i>. Do not use this class to encapsulate Materials which
     * are not potions.</b>
     *
     * @param material item material
     */
    public PotionBuilder(@NotNull Material material) {
        this(material, 1);
    }

    /**
     * An item builder from the given item.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param item The source item.
     */
    public PotionBuilder(@NotNull ItemStack item) {
        super(item);
        checkMaterial(item.getType());
    }

    /**
     * Checks weather the {@link Material} is a potion.
     * If the {@link Material} is not a potion, an {@link IllegalArgumentException} will be thrown.
     *
     * @param material The material.
     */
    private static Material checkMaterial(Material material) {
        return switch (material) {
            case POTION, LEGACY_POTION, LINGERING_POTION, LEGACY_LINGERING_POTION, SPLASH_POTION, LEGACY_SPLASH_POTION ->
                    material;
            default -> throw new IllegalArgumentException("Material must be potion.");
        };
    }

    /**
     * Set the {@link Material} of the {@link ItemStack}.
     * If the {@link Material} is not a potion, an {@link IllegalArgumentException} will be thrown.
     *
     * @param material The new {@link Material}.
     */
    @Override
    public PotionBuilder setType(@NotNull Material material) {
        item.setType(checkMaterial(material));
        return this;
    }

    /**
     * Sets the underlying potion data
     *
     * @param data PotionData to set the base potion state to
     */
    public PotionBuilder setBasePotionData(@NotNull PotionData data) {
        ((PotionMeta) meta).setBasePotionData(data);
        return this;
    }

    /**
     * Adds a custom potion effect to this potion.
     *
     * @param effect    the potion effect to add
     * @param overwrite true if any existing effect of the same type should be
     *                  overwritten
     */
    public PotionBuilder addCustomEffect(@NotNull PotionEffect effect, boolean overwrite) {
        ((PotionMeta) meta).addCustomEffect(effect, overwrite);
        return this;
    }

    /**
     * Removes a custom potion effect from this potion.
     *
     * @param type the potion effect type to remove
     */
    public PotionBuilder removeCustomEffect(@NotNull PotionEffectType type) {
        ((PotionMeta) meta).removeCustomEffect(type);
        return this;
    }

    /**
     * Moves a potion effect to the top of the potion effect list.
     * <p>
     * This causes the client to display the potion effect in the potion's name.
     *
     * @param type the potion effect type to move
     * @deprecated use {@link #setBasePotionData(org.bukkit.potion.PotionData)}
     */
    @Deprecated
    public PotionBuilder setMainEffect(@NotNull PotionEffectType type) {
        ((PotionMeta) meta).setMainEffect(type);
        return this;
    }

    /**
     * Sets the potion color. A custom potion color will alter the display of
     * the potion in an inventory slot.
     *
     * @param color the color to set
     */
    public PotionBuilder setColor(@Nullable Color color) {
        ((PotionMeta) meta).setColor(color);
        return this;
    }
}

