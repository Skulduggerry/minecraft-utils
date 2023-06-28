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

package me.skulduggerry;

import me.skulduggerry.utilities.builder.ItemBuilder;
import me.skulduggerry.utilities.template.item.ItemTemplate;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * Class to store all items required by the plugin
 *
 * @author Skulduggerry
 * @since 1.0.0
 */
public class Items {

    private Items() {
    }

    public static final String MISSING_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWM5MGNhNTA3M2M0OWI4OThhNmY4Y2RiYzcyZTZhY2EwYTQyNWVjODNiYzQzNTVlM2I4MzRmZDg1OTI4MmJkZCJ9fX0=";

    public static final ItemStack BLUE_GLASS = new ItemBuilder<>(Material.BLUE_STAINED_GLASS_PANE)
            .setDisplayName(Component.empty())
            .addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS)
            .build();

    public static final ItemTemplate CLOSE_PAGE_ITEM = player -> new ItemBuilder<>(Material.RED_STAINED_GLASS_PANE)
            .setDisplayName(LanguageAPI.getMessage(player, "ui.close"))
            .addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS)
            .build();
}
