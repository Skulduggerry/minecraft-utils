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
import me.skulduggerry.Main;
import me.skulduggerry.utilities.config.Config;
import me.skulduggerry.utilities.utils.LocaleUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Optional;

/**
 * Implementation of the language manage interface basing on a config
 *
 * @author Skulduggerry
 * @since 1.0.0
 */
public class ConfigBasedLanguageManager extends AbstractLanguageManager {

    private static final Locale DEFAULT_LANGUAGE = Locale.ENGLISH;
    private static final Path DEFAULT_CONFIG_LOCATION = Paths.get("./plugins/lang/saved_player_languages.yml");
    private static ConfigBasedLanguageManager instance;

    public static ConfigBasedLanguageManager getInstance(@NonNull Plugin plugin, boolean overrideExistingKeys) {
        if (instance == null) instance = new ConfigBasedLanguageManager(DEFAULT_LANGUAGE, DEFAULT_CONFIG_LOCATION);
        instance.registerPlugin(plugin, overrideExistingKeys);
        return instance;
    }

    private final Path configFileLocation;
    private Config playerLanguageStorage;

    /**
     * Constructor.
     *
     * @param defaultLanguage The default language for this instance
     */
    public ConfigBasedLanguageManager(@NonNull Locale defaultLanguage, Path path) {
        super(defaultLanguage);
        this.configFileLocation = path;
    }

    /**
     * get the language for a player if was not loaded
     *
     * @param player the player
     * @return the language
     */
    @Override
    protected Language fetchLanguage(@NonNull Player player) {
        return Optional.ofNullable(playerLanguageStorage.getString(player.getUniqueId().toString()))
                .map(LocaleUtils::toLocale)
                .or(() -> Optional.of(player.locale()))
                .map(LocaleUtils::onlyLanguage)
                .map(locale -> getLanguage(locale).orElseGet(this::getDefaultLanguage))
                .get();
    }

    /**
     * save the player's language
     *
     * @param player the player
     */
    @Override
    protected void storeLanguage(@NonNull Player player) {
        playerLanguageStorage.set(player.getName(), getPlayerLanguage(player).getLocale().toString());
    }

    /**
     * save the players data
     */
    @Override
    public void save() {
        getPlayerLanguages().keySet().forEach(this::storeLanguage);
    }

    /**
     * load the players data
     */
    @Override
    public void load() {
        if (playerLanguageStorage != null) return;
        playerLanguageStorage = Main.getInstance().getConfig(configFileLocation);
    }
}
