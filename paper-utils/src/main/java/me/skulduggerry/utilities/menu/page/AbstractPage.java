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
import me.skulduggerry.utilities.menu.slot.SlotImpl;
import me.skulduggerry.utilities.menu.slot.SlotSettings;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import me.skulduggerry.utilities.template.title.MenuPageTitleTemplate;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

/**
 * Abstraction of the {@link Page} interface.
 *
 * @author Skulduggerry
 * @since 1.4.0
 */
public abstract class AbstractPage implements Page {

    protected final @Range(from = 1, to = Integer.MAX_VALUE) int pageNumber;
    protected final @NotNull MenuPageTitleTemplate title;
    protected final @Range(from = 1, to = Integer.MAX_VALUE) int width;
    protected final @Range(from = 1, to = Integer.MAX_VALUE) int height;
    protected final @Nullable InventoryType type;
    protected final @NotNull Collection<PageHolder> holders = new HashSet<>();
    protected @Nullable CloseHandler closeHandler;
    protected @NotNull Slot[] slots;

    /**
     * Constructor.
     *
     * @param pageNumber   The number of the page (important for the {@link MenuPageTitleTemplate}).
     * @param title        The {@link MenuPageTitleTemplate}.
     * @param closeHandler The {@link Page.CloseHandler}.
     */
    public AbstractPage(@Range(from = 1, to = Integer.MAX_VALUE) int pageNumber,
                        @NotNull MenuPageTitleTemplate title,
                        @Nullable CloseHandler closeHandler,
                        @Range(from = 1, to = Integer.MAX_VALUE) int width,
                        @Range(from = 0, to = Integer.MAX_VALUE) int height) {
        this.pageNumber = pageNumber;
        this.title = title;
        this.closeHandler = closeHandler;
        this.width = width;
        this.height = height;
        this.type = null;
        generateSlots();
    }

    /**
     * Constructor.
     *
     * @param pageNumber   The number of the page (important for the {@link MenuPageTitleTemplate}).
     * @param title        The {@link MenuPageTitleTemplate}.
     * @param closeHandler The {@link Page.CloseHandler}.
     * @param type         The {@link InventoryType} of the page.
     */
    public AbstractPage(@Range(from = 1, to = Integer.MAX_VALUE) int pageNumber,
                        @NotNull MenuPageTitleTemplate title,
                        @Nullable CloseHandler closeHandler,
                        @NotNull InventoryType type) {
        this.pageNumber = pageNumber;
        this.title = Objects.requireNonNull(title);
        this.closeHandler = closeHandler;
        this.type = Objects.requireNonNull(type);
        this.width = -1;
        this.height = -1;
        generateSlots();
    }

    /**
     * Helper method which creates the slots.
     */
    private void generateSlots() {
        slots = new Slot[getSize()];

        for (int i = 0; i < getSize(); i++) {
            slots[i] = new SlotImpl(i, this);
        }
    }

    /**
     * Get the holders of pages.
     * Important to check whether an opened inventory is a page.
     *
     * @return A {@link Collection} of {@link PageHolder}s.
     */
    @Override
    public Collection<PageHolder> getHolders() {
        return Collections.unmodifiableCollection(holders);
    }

    /**
     * Get the player who are watching at this page.
     *
     * @return A {@link Collection} of {@link Player}s.
     */
    @Override
    public Collection<Player> getViewers() {
        return holders.stream().map(PageHolder::getPlayer).toList();
    }

    /**
     * Checks weather a player has opened this page.
     *
     * @param player The player.
     * @return The result of the check.
     */
    @Override
    public boolean isOpen(@NotNull Player player) {
        return getViewers().contains(player);
    }

    /**
     * Opens the page for the player.
     *
     * @param player The player.
     */
    @Override
    public void open(@NotNull Player player) {

        InventoryHolder currentHolder = player.getOpenInventory().getTopInventory().getHolder();

        if (currentHolder instanceof PageHolder holder) { // change the attributes of the PageHolder
            Page open = holder.getPage();

            if (open == this) return;

            open.closedByPlayer(holder.getPlayer(), false); // remove the PageHolder from the closed page

            Inventory inventory = createInventory(holder);
            holder.setInventory(inventory); // set the new attributes of the PageHolder
            holder.setPage(this); // set the new attributes of the PageHolder
            player.openInventory(inventory);
            holders.add(holder);
            return;
        }

        // create new PageHolder of not present
        PageHolder holder = new PageHolder(player, this);
        Inventory inventory = createInventory(holder);
        holder.setInventory(inventory);
        player.openInventory(inventory);
        holders.add(holder);
    }

    /**
     * Creates a new inventory with the given title and the given items.
     *
     * @param holder The holder of the inventory (used to identify inventories which represents pages).
     * @return The new inventory.
     */
    private Inventory createInventory(PageHolder holder) {
        Inventory inventory = type == null ?
                Bukkit.createInventory(holder, getSize(), title.getTitle(holder.getPlayer(), pageNumber)) :
                Bukkit.createInventory(holder, type, title.getTitle(holder.getPlayer(), pageNumber));
        updateInventoryContent(holder.getPlayer(), inventory);
        return inventory;
    }

    /**
     * Closes this page for all players.
     */
    @Override
    public void close() {
        getViewers().forEach(this::close);
    }

    /**
     * Closes this page for the given player.
     *
     * @param player The player.
     */
    @Override
    public void close(@NotNull Player player) {
        closedByPlayer(player, true);
        player.closeInventory();
    }

    /**
     * Called when this page is closed by the player.
     *
     * @param player          The player.
     * @param activateTrigger Option to trigger the {@link #getCloseHandler()} if present.
     */
    @Override
    public void closedByPlayer(@NotNull Player player, boolean activateTrigger) {
        InventoryHolder invHolder = player.getOpenInventory().getTopInventory().getHolder();

        if (!(invHolder instanceof PageHolder holder) || !holders.contains(holder)) return;

        holders.remove(holder);
        if (activateTrigger) {
            getCloseHandler().ifPresent(closeHandler -> closeHandler.handleClose(player, this));
        }
    }

    /**
     * Tests if the slot is in bounds
     *
     * @param slot The slot
     */
    private void testSlotIndex(int slot) {
        if (0 > slot || slot >= getSize()) throw new SlotIndexOutOfBoundsException(slot);
    }

    /**
     * Updates a single slot.
     *
     * @param slot The slot to update.
     */
    @Override
    public void updateSlot(int slot) {
        testSlotIndex(slot);
        getViewers().forEach(player -> updateSlot(slot, player));
    }

    /**
     * Updates a single slot for the given player.
     *
     * @param slot   The slot to update.
     * @param player The player.
     */
    @Override
    public void updateSlot(int slot, @NotNull Player player) {
        testSlotIndex(slot);

        InventoryHolder invHolder = player.getOpenInventory().getTopInventory().getHolder();
        if (!(invHolder instanceof PageHolder holder) || !holders.contains(holder)) return;
        Inventory inventory = invHolder.getInventory();
        ItemStack item = getSlot(slot).getItem(player);
        inventory.setItem(slot, item);
    }

    /**
     * Updates this page for all players.
     */
    @Override
    public void update() {
        getViewers().forEach(this::update);
    }

    /**
     * Updates this page for the given player.
     *
     * @param player The player.
     */
    @Override
    public void update(@NotNull Player player) {
        InventoryHolder invHolder = player.getOpenInventory().getTopInventory().getHolder();
        if (!(invHolder instanceof PageHolder holder) || !holders.contains(holder)) return;
        updateInventoryContent(player, holder.getInventory());
    }

    /**
     * Recreates the full page for all players.
     * Necessary e.g. when the title changes.
     */
    @Override
    public void updateFull() {
        getViewers().forEach(this::updateFull);
    }

    /**
     * Recreates the full page for the given player.
     * Necessary e.g. when the title changes.
     *
     * @param player The player.
     */
    @Override
    public void updateFull(@NotNull Player player) {
        InventoryHolder invHolder = player.getOpenInventory().getTopInventory().getHolder();
        if (!(invHolder instanceof PageHolder holder) || !holders.contains(holder)) return;
        Inventory inventory = createInventory(holder);
        holder.setInventory(inventory); // set the new attributes of the PageHolder
        player.openInventory(inventory);
    }

    /**
     * Updates the contest of the inventory for the given player.
     *
     * @param player    The player.
     * @param inventory The inventory.
     */
    private void updateInventoryContent(Player player, Inventory inventory) {
        for (Slot slot : slots) {
            inventory.setItem(slot.getIndex(), slot.getItem(player));
        }
        player.updateInventory();
    }

    /**
     * Get the slot at the given index.
     * If index is less 0 or index is more than the size of the page an {@link ArrayIndexOutOfBoundsException} will be thrown.
     *
     * @param index The index.
     * @return The slot at the given index.
     */
    @Override
    public Slot getSlot(int index) {
        testSlotIndex(index);
        return slots[index];
    }

    /**
     * Removes all items from the page.
     */
    @Override
    public void clear() {
        for (Slot slot : slots) {
            slot.setSlotSettings(SlotSettings.builder().build());
        }
    }

    /**
     * Clears the slot at the given index.
     * If index is less 0 or index is more than the size of the page an {@link ArrayIndexOutOfBoundsException} will be thrown.
     *
     * @param index The index.
     */
    @Override
    public void clear(int index) {
        testSlotIndex(index);
        getSlot(index).setSlotSettings(SlotSettings.builder().build());
    }

    /**
     * Get the size of the page.
     *
     * @return The size.
     */
    @Override
    public int getSize() {
        return type == null ? getWidth() * getHeight() : type.getDefaultSize();
    }

    /**
     * Get the width of this page.
     *
     * @return The width.
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of this page.
     *
     * @return The height.
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Get the index of the first empty slot or -1 of all slots are full.
     * A slot is empty when no {@link ItemTemplate} is set.
     *
     * @return The index.
     */
    @Override
    public int getFirstEmpty() {
        for (Slot slot : slots) {
            if (slot.isEmpty()) return slot.getIndex();
        }
        return -1;
    }

    /**
     * Get the {@link CloseHandler} of this page wrapped in a {@link Optional}.
     *
     * @return The wrapped {@link CloseHandler}.
     */
    @Override
    public Optional<CloseHandler> getCloseHandler() {
        return Optional.ofNullable(closeHandler);
    }

    /**
     * Set the{@link CloseHandler} of this page.
     *
     * @param closeHandler the new {@link CloseHandler}.
     */
    @Override
    public void setCloseHandler(@Nullable CloseHandler closeHandler) {
        this.closeHandler = closeHandler;
    }

    /**
     * Abstraction of the {@link Page.Builder} interface.
     *
     * @param <T> The type of the builder.
     * @author Skulduggerry
     * @since 0.1.0
     */
    public static abstract class Builder<T extends Builder<T>> implements Page.Builder<T> {
        private MenuPageTitleTemplate titleTemplate;
        private CloseHandler closeHandler;

        /**
         * Constructor.
         *
         * @param template The {@link MenuPageTitleTemplate}.
         */
        protected Builder(@NotNull MenuPageTitleTemplate template) {
            title(template);
        }

        /**
         * Constructor.
         *
         * @param title The static title.
         */
        protected Builder(@NotNull Component title) {
            title(title);
        }

        /**
         * Set the {@link MenuPageTitleTemplate}.
         *
         * @param titleTemplate The new {@link MenuPageTitleTemplate}.
         * @return The builder.
         */
        @SuppressWarnings("unchecked")
        @Override
        public T title(@NotNull MenuPageTitleTemplate titleTemplate) {
            this.titleTemplate = Objects.requireNonNull(titleTemplate);
            return (T) this;
        }

        /**
         * Set a static title for all players.
         *
         * @param title The title.
         * @return the builder.
         */
        @Override
        public T title(@NotNull Component title) {
            return title((receiver, page) -> title);
        }

        /**
         * Get the {@link MenuPageTitleTemplate} for this builder.
         *
         * @return The {@link MenuPageTitleTemplate}.
         */
        @NotNull
        public MenuPageTitleTemplate title() {
            return titleTemplate;
        }

        /**
         * Set the {@link CloseHandler} for this builder.
         *
         * @param closeHandler The new {@link CloseHandler}.
         * @return the builder.
         */
        @SuppressWarnings("unchecked")
        @Override
        public T closeHandler(@Nullable CloseHandler closeHandler) {
            this.closeHandler = closeHandler;
            return (T) this;
        }

        /**
         * Get the {@link CloseHandler}.
         *
         * @return The {@link CloseHandler}.
         */
        @Nullable
        public CloseHandler closeHandler() {
            return closeHandler;
        }

        /**
         * Build the page with the given attributes and set the pageNumber automatically to one.
         *
         * @return The new page.
         */
        @Override
        public Page build() {
            return build(1);
        }
    }
}
