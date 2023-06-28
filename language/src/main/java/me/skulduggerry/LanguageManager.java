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

import me.skulduggerry.exception.MissingLanguageException;
import me.skulduggerry.utilities.manager.Manager;
import me.skulduggerry.utilities.template.message.MessageTemplate;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * Interface which represents a manager of languages.
 * Allows every player to his own language.
 *
 * @author Skulduggerry
 * @since 1.0.0
 */
public interface LanguageManager extends Manager {

    /**
     * Get a collection of all available languages
     *
     * @return all languages
     */
    @NotNull
    Collection<Language> getAvailableLanguages();

    /**
     * checks whether the given locale is supported
     *
     * @param locale the locale to check
     * @return the result
     */
    boolean containsLanguage(@NotNull Locale locale);

    /**
     * get the default language
     *
     * @return the language
     */
    @NotNull
    Language getDefaultLanguage();

    /**
     * get a language by a locale by a locale
     *
     * @param locale the locale
     * @return the language wrapped in an optional
     */
    @NotNull
    Optional<Language> getLanguage(@NotNull Locale locale);

    /**
     * unregisters a player from the manager to save memory
     *
     * @param player the player
     */
    void unregisterPlayerIfLoaded(@NotNull Player player);

    /**
     * get the language of the player.
     *
     * @param player the player
     * @return the language
     */
    @NotNull
    Language getPlayerLanguage(@NotNull Player player);

    /**
     * set the language of a player.
     * throws a {@link MissingLanguageException} when the language is not supported
     *
     * @param player the player
     * @param locale the new language
     */
    void setPlayerLanguage(@NotNull Player player, @NotNull Locale locale);

    /**
     * get a message for a given player
     *
     * @param player the player
     * @param key    the message key
     * @return the message
     */
    @NotNull
    Component getMessage(@NotNull Player player, @NotNull String key);

    /**
     * get a formatted message for the given player
     *
     * @param player the player
     * @param key    the message key
     * @param args   the formatting arguments
     * @return the formatted message
     */
    @NotNull
    Component getMessage(@NotNull Player player, @NotNull String key, @NotNull Object[] args);

    /**
     * get a message template from the given key
     *
     * @param key the message key
     * @return the template
     */
    @NotNull
    MessageTemplate toMessageTemplate(@NotNull String key);

    /**
     * registers a plugin so it can use its defined messages
     *
     * @param plugin               the plugin to load from
     * @param overrideExistingKeys should this method override existing keys for languages
     */
    void registerPlugin(@NotNull Plugin plugin, boolean overrideExistingKeys);

    /**
     * save the players data
     */
    void save();

    /**
     * load the players data
     */
    void load();
}
