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

import me.skulduggerry.utilities.template.message.MessageTemplate;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class LanguageAPI {

    /**
     * Constructor
     */
    private LanguageAPI() {
    }

    /**
     * get a message for a given player
     *
     * @param player the player
     * @param key    the message key
     * @return the message
     */
    @NotNull
    public static Component getMessage(@NotNull Player player, @NotNull String key) {
        return Main.getInstance().getLanguageManager().getMessage(player, key);
    }

    /**
     * get a formatted message for the given player
     *
     * @param player the player
     * @param key    the message key
     * @param args   the formatting arguments
     * @return the formatted message
     */
    @NotNull
    public static Component getMessage(@NotNull Player player, @NotNull String key, @NotNull Object[] args) {
        return Main.getInstance().getLanguageManager().getMessage(player, key, args);
    }

    /**
     * get a message template from the given key
     *
     * @param key the message key
     * @return the template
     */
    @NotNull
    public static MessageTemplate toMessageTemplate(@NotNull String key) {
        return Main.getInstance().getLanguageManager().toMessageTemplate(key);
    }

    /**
     * registers a plugin so it can use its defined messages
     *
     * @param plugin               the plugin to load from
     * @param overrideExistingKeys should this method override existing keys for languages
     */
    public static void register(@NotNull Plugin plugin, boolean overrideExistingKeys) {
        Main.getInstance().getLanguageManager().registerPlugin(plugin, overrideExistingKeys);
        Main.getInstance().getUiManager().updateMenu();
    }
}
