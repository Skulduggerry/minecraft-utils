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

package me.skulduggerry.utilities.menu;

import me.skulduggerry.utilities.menu.page.Page;
import me.skulduggerry.utilities.menu.page.PageHolder;
import me.skulduggerry.utilities.menu.slot.Slot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import static org.bukkit.event.inventory.InventoryAction.MOVE_TO_OTHER_INVENTORY;
import static org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import static org.bukkit.event.inventory.InventoryCloseEvent.Reason.*;

/**
 * Class with all listeners which are important for working with {@link Menu}s and {@link Page}s.
 * Your main class must register this listener to work with menus.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class MenuFunctionListener implements Listener {

    /**
     * Handles clicks to an inventory.
     *
     * @param event The event.
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        Inventory clicked = event.getClickedInventory();
        Inventory top = view.getTopInventory();

        if (!(top.getHolder() instanceof PageHolder holder)) {
            return;
        }

        if (top != clicked) {
            if (event.getAction() == MOVE_TO_OTHER_INVENTORY)
                event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Page page = holder.getPage();
        Inventory inventory = holder.getInventory();
        Slot slot = page.getSlot(event.getSlot());
        InventoryAction action = event.getAction();
        ClickType click = event.getClick();

        event.setCancelled(true);
        slot.getClickHandler().ifPresent(clickHandler -> clickHandler.handleClick(new ClickInformation(event, player, page, inventory, slot, action, click)));
    }

    /**
     * Cancels the drag on pages.
     *
     * @param event The event.
     */
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        InventoryView view = event.getView();
        Inventory top = view.getTopInventory();

        if (top.getHolder() instanceof PageHolder)
            event.setCancelled(true);
    }

    /**
     * Handles the close of pages.
     *
     * @param event the event.
     */
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory top = event.getView().getTopInventory();
        Reason reason = event.getReason();

        if (!(top.getHolder() instanceof PageHolder holder) || reason == OPEN_NEW || reason == PLUGIN || reason == DEATH) {
            return;
        }

        Page page = holder.getPage();
        page.closedByPlayer((Player) event.getPlayer(), true);
    }

    /**
     * Cancels the drop of items on pages.
     *
     * @param event The event.
     */
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        InventoryView view = event.getPlayer().getOpenInventory();
        Inventory top = view.getTopInventory();

        if (top.getHolder() instanceof PageHolder)
            event.setCancelled(true);
    }
}
