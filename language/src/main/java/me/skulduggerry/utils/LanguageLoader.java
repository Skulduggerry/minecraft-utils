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

package me.skulduggerry.utils;

import me.skulduggerry.Language;
import me.skulduggerry.utilities.config.Config;
import me.skulduggerry.utilities.config.type.YamlConfig;
import me.skulduggerry.utilities.utils.LocaleUtils;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

/**
 * Helper class to load language files from a plugin.
 *
 * @author Skulduggerry
 * @since 1.0.0
 */
public class LanguageLoader {

    /**
     * Constructor
     */
    private LanguageLoader() {
    }

    /**
     * Load all languages from plugin from "/lang".
     * Available languages must be configured in a file called "languages.yml" with a string-list "available".
     * Focuses only on the language part of the locale string.
     *
     * @param plugin The plugin.
     * @return The provided languages
     */
    public static Collection<Language> loadLanguages(Plugin plugin) {
        return getLanguageResource(plugin, "languages").getStringList("available").stream()
                .map(LocaleUtils::toLocale)
                .map(LocaleUtils::onlyLanguage)
                .map(locale -> loadLanguage(plugin, locale))
                .toList();
    }

    /**
     * Load a language with the given locale
     *
     * @param plugin The plugin to load from
     * @param locale The language to load
     * @return The language
     */
    private static Language loadLanguage(Plugin plugin, Locale locale) {
        Config config = getLanguageResource(plugin, locale.toString().toLowerCase());
        locale = new Locale(locale.getLanguage());
        return new Language(locale, config);
    }

    /**
     * Get a language resource from the given plugin.
     * Those resources must be in the folder "lang" and must end with .yml or .yaml
     *
     * @param plugin       The plugin
     * @param resourceName The name of the resource
     * @return The config which is located at the path
     */
    private static Config getLanguageResource(Plugin plugin, String resourceName) {
        String path = "lang/" + resourceName;
        InputStream stream = plugin.getResource(path.concat(".yml"));
        if (stream == null) stream = Objects.requireNonNull(plugin.getResource(path.concat(".yaml")));
        return Config.load(new InputStreamReader(stream), YamlConfig.class);
    }
}
