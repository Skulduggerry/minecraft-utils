package me.skulduggerry.utilities.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class which allows the easy creation of items using the builder pattern.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class ItemBuilder<T extends ItemBuilder<T>> {

    protected final ItemStack item;
    protected final ItemMeta meta;

    /**
     * An item builder from the given item.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param item The source item.
     */
    public ItemBuilder(@NotNull ItemStack item) {
        this.item = item.clone();
        meta = this.item.getItemMeta();
    }

    /**
     * An item builder with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param material item material
     * @param amount   stack size
     */
    public ItemBuilder(@NotNull Material material, @Range(from = 1, to = 64) int amount) {
        this(new ItemStack(material, amount));
    }

    /**
     * An item builder with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param material item material
     */
    public ItemBuilder(@NotNull Material material) {
        this(material, 1);
    }

    /**
     * Sets the type of this item
     * <p>
     * Note that in doing so you will reset the MaterialData for this stack.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param material New type to set the items in this stack to
     */
    @SuppressWarnings("unchecked")
    public T setType(@NotNull Material material) {
        item.setType(material);
        return (T) this;
    }

    /**
     * Sets the amount of items in this stack
     *
     * @param amount New amount of items in this stack
     */
    @SuppressWarnings("unchecked")
    public T setAmount(@Range(from = 1, to = 64) int amount) {
        item.setAmount(amount);
        return (T) this;
    }

    /**
     * Sets the MaterialData for this stack of items
     *
     * @param data New MaterialData for this item
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public T setData(@NotNull MaterialData data) {
        item.setData(data);
        return (T) this;
    }

    /**
     * Adds the specified enchantments to this item stack.
     * <p>
     * This method is the same as calling {@link
     * #addEnchantment(org.bukkit.enchantments.Enchantment, int)} for each
     * element of the map.
     *
     * @param enchantments Enchantments to add
     * @throws IllegalArgumentException if the specified enchantments is null
     * @throws IllegalArgumentException if any specific enchantment or level
     *                                  is null. <b>Warning</b>: Some enchantments may be added before this
     *                                  exception is thrown.
     */
    @SuppressWarnings("unchecked")
    public T addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        item.addEnchantments(enchantments);
        return (T) this;
    }

    /**
     * Adds the specified {@link Enchantment} to this item stack.
     * <p>
     * If this item stack already contained the given enchantment (at any
     * level), it will be replaced.
     *
     * @param enchantment Enchantment to add
     * @param level       Level of the enchantment
     * @throws IllegalArgumentException if enchantment null, or enchantment is
     *                                  not applicable
     */
    @SuppressWarnings("unchecked")
    public T addEnchantment(@NotNull Enchantment enchantment, @Range(from = 1, to = Integer.MAX_VALUE) int level) {
        item.addEnchantment(enchantment, level);
        return (T) this;
    }

    /**
     * Adds the specified enchantments to this item stack in an unsafe manner.
     * <p>
     * This method is the same as calling {@link
     * #addUnsafeEnchantment(org.bukkit.enchantments.Enchantment, int)} for
     * each element of the map.
     *
     * @param enchantments Enchantments to add
     */
    @SuppressWarnings("unchecked")
    public T addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        item.addUnsafeEnchantments(enchantments);
        return (T) this;
    }

    /**
     * Adds the specified {@link Enchantment} to this item stack.
     * <p>
     * If this item stack already contained the given enchantment (at any
     * level), it will be replaced.
     * <p>
     * This method is unsafe and will ignore level restrictions or item type.
     * Use at your own discretion.
     *
     * @param enchantment Enchantment to add
     * @param level       Level of the enchantment
     */
    @SuppressWarnings("unchecked")
    public T addUnsafeEnchantment(@NotNull Enchantment enchantment, @Range(from = 1, to = Integer.MAX_VALUE) int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return (T) this;
    }

    /**
     * Removes the specified {@link Enchantment} if it exists on this
     * ItemStack
     *
     * @param enchantment Enchantment to remove
     */
    @SuppressWarnings("unchecked")
    public T removeEnchantment(@NotNull Enchantment enchantment) {
        item.removeEnchantment(enchantment);
        return (T) this;
    }

    /**
     * Sets the display name.
     *
     * @param displayName the name to set
     */
    @SuppressWarnings("unchecked")
    public T setDisplayName(@NotNull Component displayName) {
        meta.displayName(displayName);
        return (T) this;
    }

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set
     */
    @SuppressWarnings("unchecked")
    public T setLore(@NotNull Component @NotNull ... lore) {
        meta.lore(List.of(lore));
        return (T) this;
    }

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set
     */
    @SuppressWarnings("unchecked")
    public T setLore(@NotNull List<Component> lore) {
        meta.lore(lore);
        return (T) this;
    }

    /**
     * Add an Attribute and it's Modifier.
     * AttributeModifiers can now support {@link EquipmentSlot}s.
     * If not set, the {@link AttributeModifier} will be active in ALL slots.
     * <br>
     * Two {@link AttributeModifier}s that have the same {@link java.util.UUID}
     * cannot exist on the same Attribute.
     *
     * @param attribute the {@link Attribute} to modify
     * @param modifier  the {@link AttributeModifier} specifying the modification
     * @throws NullPointerException     if Attribute is null
     * @throws NullPointerException     if AttributeModifier is null
     * @throws IllegalArgumentException if AttributeModifier already exists
     */
    @SuppressWarnings("unchecked")
    public T addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        meta.addAttributeModifier(attribute, modifier);
        return (T) this;
    }

    /**
     * Set item flags which should be ignored when rendering a ItemStack in the Client. This Method does silently ignore double set itemFlags.
     *
     * @param itemFlags The hide-flags which shouldn't be rendered
     */
    @SuppressWarnings("unchecked")
    public T addItemFlags(@NotNull ItemFlag @NotNull ... itemFlags) {
        meta.addItemFlags(itemFlags);
        return (T) this;
    }

    /**
     * Remove specific set of itemFlags. This tells the Client it should render it again. This Method does silently ignore double removed itemFlags.
     *
     * @param itemFlags Hide-flags which should be removed
     */
    @SuppressWarnings("unchecked")
    public T removeItemFlags(@NotNull ItemFlag @NotNull ... itemFlags) {
        meta.removeItemFlags(itemFlags);
        return (T) this;
    }

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     *
     * @param unbreakable true if set unbreakable
     */
    @SuppressWarnings("unchecked")
    public T setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return (T) this;
    }

    /**
     * Sets the damage.
     * Does nothing of the item is not damageable.
     *
     * @param damage item damage
     */
    @SuppressWarnings("unchecked")
    public T setDamage(short damage) {
        if (meta instanceof Damageable damageableMeta)
            damageableMeta.setDamage(damage);
        return (T) this;
    }

    /**
     * Sets set of materials what given item can destroy in {@link org.bukkit.GameMode#ADVENTURE}
     *
     * @param canDestroy Set of materials
     * @deprecated Minecraft does not limit this to the material enum, Use {@link #setDestroyableKeys(Collection)} as a replacement
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public T setCanDestroy(Set<Material> canDestroy) {
        meta.setCanDestroy(canDestroy);
        return (T) this;
    }

    /**
     * Sets set of materials where given item can be placed on in {@link org.bukkit.GameMode#ADVENTURE}
     *
     * @param canPlaceOn Set of materials
     * @deprecated Minecraft does not limit this to the material enum, Use {@link #setPlaceableKeys(Collection)} as a replacement
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public T setCanPlaceOn(Set<org.bukkit.Material> canPlaceOn) {
        meta.setCanPlaceOn(canPlaceOn);
        return (T) this;
    }

    /**
     * Sets the collection of namespaced keys that the item can destroy in {@link org.bukkit.GameMode#ADVENTURE}
     *
     * @param canDestroy Collection of {@link com.destroystokyo.paper.Namespaced}
     */
    @SuppressWarnings("unchecked")
    public T setDestroyableKeys(@NotNull Collection<com.destroystokyo.paper.Namespaced> canDestroy) {
        meta.setDestroyableKeys(canDestroy);
        return (T) this;
    }

    /**
     * Sets the set of namespaced keys that the item can be placed on in {@link org.bukkit.GameMode#ADVENTURE}
     *
     * @param canPlaceOn Collection of {@link com.destroystokyo.paper.Namespaced}
     */
    @SuppressWarnings("unchecked")
    public T setPlaceableKeys(@NotNull Collection<com.destroystokyo.paper.Namespaced> canPlaceOn) {
        meta.setPlaceableKeys(canPlaceOn);
        return (T) this;
    }

    /**
     * Builds the item.
     *
     * @return The item.
     */
    @NotNull
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}