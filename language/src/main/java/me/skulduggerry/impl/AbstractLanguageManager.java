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

package me.skulduggerry.impl;

import me.skulduggerry.Language;
import me.skulduggerry.LanguageManager;
import me.skulduggerry.exception.MissingLanguageException;
import me.skulduggerry.utilities.template.message.MessageTemplate;
import me.skulduggerry.utilities.utils.LocaleUtils;
import me.skulduggerry.utils.LanguageLoader;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Implementation of the basic language manager methods.
 *
 * @author Skulduggerry
 * @since 1.0.0
 */
public abstract class AbstractLanguageManager implements LanguageManager {

    private final Set<Plugin> registered;
    private final Map<Locale, Language> availableLanguages;
    private final Map<Player, Language> playerLanguages;
    private final Locale defaultLanguage;

    /**
     * Constructor.
     */
    protected AbstractLanguageManager(Locale locale) {
        defaultLanguage = LocaleUtils.onlyLanguage(locale);
        registered = new HashSet<>();
        availableLanguages = new HashMap<>();
        playerLanguages = new HashMap<>();
    }

    /**
     * Get a collection of all available languages
     *
     * @return all languages
     */
    @Override
    public @NotNull Collection<Language> getAvailableLanguages() {
        return availableLanguages.values();
    }

    /**
     * checks whether the given locale is supported
     *
     * @param locale the locale to check
     * @return the result
     */
    @Override
    public boolean containsLanguage(@NotNull Locale locale) {
        locale = LocaleUtils.onlyLanguage(locale);
        return availableLanguages.containsKey(locale);
    }

    /**
     * get the default language
     *
     * @return the language
     */
    @Override
    public @NotNull Language getDefaultLanguage() {
        return getLanguage(defaultLanguage).orElseThrow(() -> new MissingLanguageException("Default language '%s' was not loaded!".formatted(defaultLanguage.toString())));
    }

    /**
     * get a language by a locale by a locale
     *
     * @param locale the locale
     * @return the language wrapped in an optional
     */
    @NotNull
    @Override
    public Optional<Language> getLanguage(@NotNull Locale locale) {
        locale = LocaleUtils.onlyLanguage(locale);
        return Optional.ofNullable(availableLanguages.get(locale));
    }

    /**
     * Get the map with all player languages.
     * this map is unmodifiable
     *
     * @return the map of players
     */
    @NotNull
    protected Map<Player, Language> getPlayerLanguages() {
        return Collections.unmodifiableMap(playerLanguages);
    }

    /**
     * unregisters a player from the manager to save memory
     *
     * @param player the player
     */
    @Override
    public void unregisterPlayerIfLoaded(@NotNull Player player) {
        playerLanguages.remove(player);
    }

    /**
     * get the language of the player.
     *
     * @param player the player
     * @return the language
     */
    @Override
    public @NotNull Language getPlayerLanguage(@NotNull Player player) {
        return playerLanguages.computeIfAbsent(player, this::fetchLanguage);
    }

    /**
     * set the language of a player.
     * throws a {@link MissingLanguageException} when the language is not supported
     *
     * @param player the player
     * @param locale the new language
     */
    @Override
    public void setPlayerLanguage(@NotNull Player player, @NotNull Locale locale) {
        Language language = getLanguage(locale).orElseThrow(() -> new MissingLanguageException(locale));
        playerLanguages.put(player, language);
        storeLanguage(player);
    }

    /**
     * get the language for a player if was not loaded
     *
     * @param player the player
     * @return the language
     */
    protected abstract Language fetchLanguage(@NotNull Player player);

    /**
     * save the player's language
     *
     * @param player the player
     */
    protected abstract void storeLanguage(@NotNull Player player);


    /**
     * get a message for a given player
     *
     * @param player the player
     * @param key    the message key
     * @return the message
     */
    @Override
    public @NotNull Component getMessage(@NotNull Player player, @NotNull String key) {
        return getPlayerLanguage(player).getMessage(key);
    }

    /**
     * get a formatted message for the given player
     *
     * @param player the player
     * @param key    the message key
     * @param args   the formatting arguments
     * @return the formatted message
     */
    @Override
    public @NotNull Component getMessage(@NotNull Player player, @NotNull String key, @NotNull Object[] args) {
        return getPlayerLanguage(player).getMessage(key, args);
    }

    /**
     * get a message template from the given key
     *
     * @param key the message key
     * @return the template
     */
    @Override
    public @NotNull MessageTemplate toMessageTemplate(@NotNull String key) {
        return (receiver, args) -> (receiver instanceof Player player ? getPlayerLanguage(player) : getDefaultLanguage()).getMessage(key);
    }

    /**
     * registers a plugin so it can use its defined messages
     *
     * @param plugin               the plugin to load from
     * @param overrideExistingKeys should this method override existing keys for languages
     */
    @Override
    public void registerPlugin(@NotNull Plugin plugin, boolean overrideExistingKeys) {
        if (!registered.add(plugin)) return;

        Queue<Language> notRegistered = new LinkedList<>();
        for (Language language : LanguageLoader.loadLanguages(plugin)) {
            Locale locale = language.getLocale();

            if (availableLanguages.containsKey(locale)) {
                availableLanguages.get(locale).addLanguageKeys(language, overrideExistingKeys);
            } else {
                availableLanguages.put(locale, language);
                notRegistered.add(language);
            }
        }

        if (notRegistered.isEmpty()) return;
        Language defaultLanguage = getDefaultLanguage();
        while (!notRegistered.isEmpty()) {
            notRegistered.remove().setFallbackLanguage(defaultLanguage);
        }
    }

    /**
     * Handle enable of the manager.
     */
    @Override
    public void handleEnable() {
        load();
    }

    /**
     * Handle disable of the manager.
     */
    @Override
    public void handleDisable() {
        save();
    }
}
