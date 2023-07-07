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

package me.skulduggerry.utilities.template.title;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Interface which represents the template for a message.
 * Allows different message for every receiver.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
@FunctionalInterface
public interface MenuPageTitleTemplate {

    /**
     * Get a message template which gives the same message for every player and page.
     *
     * @param title The title.
     * @return The template.
     */
    static MenuPageTitleTemplate of(@NotNull String title) {
        return (receiver, page) -> MiniMessage.miniMessage().deserialize(title);
    }

    /**
     * Get a message template which gives the same message for every player but depends on the page number.
     *
     * @param title The title.
     * @return The template.
     */
    static MenuPageTitleTemplate ofNumbered(@NotNull String title) {
        return (receiver, page) -> MiniMessage.miniMessage().deserialize(title)
                .append(Component.text(" | ").color(NamedTextColor.GRAY))
                .append(Component.text(page).color(NamedTextColor.BLACK));
    }

    /**
     * Get the message for the given player.
     * Every player can get a unique message.
     *
     * @param receiver The player.
     * @return The message.
     */
    Component getTitle(@NotNull Player receiver, @Range(from = 1, to = Integer.MAX_VALUE) int page);
}
