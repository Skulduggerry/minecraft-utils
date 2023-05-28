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

package me.skulduggerry.utilities.menu.slot;

import me.skulduggerry.utilities.menu.page.Page;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of the {@link Slot} interface.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class SlotImpl implements Slot {

    private final int index;
    private final Page page;
    private ItemTemplate template;
    private ClickHandler clickHandler;

    /**
     * Constructor.
     *
     * @param index The index of the slot.
     * @param page  the page of the slot.
     */
    public SlotImpl(@Range(from = 0, to = Integer.MAX_VALUE) int index, @NotNull Page page) {
        this.index = index;
        this.page = Objects.requireNonNull(page);
    }

    /**
     * Get the index of the slot.
     *
     * @return The index.
     */
    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int getIndex() {
        return index;
    }

    /**
     * Get the item of the slot for the given player.
     *
     * @param player the player.
     * @return The item.
     */
    @Override
    public ItemStack getItem(@NotNull Player player) {
        return template != null ? template.getItem(player) : null;
    }

    /**
     * Set the template for the items.
     *
     * @param template The template.
     */
    @Override
    public void setItemTemplate(@Nullable ItemTemplate template) {
        this.template = template;
        update();
    }

    /**
     * Checks weather the slot is empty.
     * A slot is empty when no {@link ItemTemplate} is set.
     *
     * @return The result of the check.
     */
    @Override
    public boolean isEmpty() {
        return template == null;
    }

    /**
     * Get the click handler wrapped in an {@link Optional} because it can be null.
     *
     * @return The wrapped click handler.
     */
    @Override
    public @NotNull Optional<ClickHandler> getClickHandler() {
        return Optional.ofNullable(clickHandler);
    }

    /**
     * Set the click handler.
     *
     * @param clickHandler The new click handler.
     */
    @Override
    public void setClickHandler(@Nullable ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    /**
     * Updates the current slot
     */
    @Override
    public void update() {
        page.updateSlot(getIndex());
    }

    /**
     * Updates the current slot
     *
     * @param player The player to update the slot for
     */
    @Override
    public void update(@NotNull Player player) {
        page.updateSlot(getIndex(), player);
    }

    /**
     * Set slot a {@link SlotSettings} to the slot.
     * Sets the {@link ItemTemplate} and the {@link ClickHandler}.
     *
     * @param settings The setting.
     */
    @Override
    public void setSlotSettings(@NotNull SlotSettings settings) {
        Objects.requireNonNull(settings);
        setItemTemplate(settings.getItemTemplate());
        setClickHandler(settings.getClickHandler());
    }
}
