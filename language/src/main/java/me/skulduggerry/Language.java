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

import me.skulduggerry.utilities.builder.SkullBuilder;
import me.skulduggerry.utilities.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.Optional;

/**
 * Represent a language with custom messages.
 *
 * @author Skulduggerry
 * @since 1.0.0
 */
public class Language {

    private Language fallbackLanguage;
    private final Locale locale;
    private final Config config;
    private ItemStack item;

    /**
     * Constructor.
     *
     * @param locale Locale code of this language
     * @param config Config which contains the key-message-pairs
     */
    public Language(@NotNull Locale locale, @NotNull Config config) {
        this(null, locale, config);
    }

    /**
     * Constructor
     *
     * @param fallbackLanguage The default language
     * @param locale           Locale code of this language
     * @param config           Config which contains the key-message-pairs
     */
    public Language(Language fallbackLanguage, Locale locale, Config config) {
        setFallbackLanguage(fallbackLanguage);
        this.locale = locale;
        this.config = config;
        loadItem();
    }

    /**
     * Get a message by a given key or from the default language.
     * If there is no message with the given key it will return a message which says which key it could not find
     *
     * @param key The key.
     * @return The message.
     */
    @NotNull
    private String getMessage0(@NotNull String key) {
        return Optional.ofNullable(config.getString(key))
                .or(() -> getFallbackLanguage().map(language -> language.getMessage0(key)))
                .orElse(key);
    }

    /**
     * Get a message by a given key or from the default language.
     * If there is no message with the given key it will return a message which says which key it could not find
     *
     * @param key The key.
     * @return The message.
     */
    @NotNull
    public Component getMessage(@NotNull String key) {
        String plainText = getMessage0(key);
        return MiniMessage.miniMessage().deserialize(plainText);
    }

    /**
     * Get a message and format it with the given arguments.
     * If there is no message with the given key it will return a message which says which key it could not find.
     * If the formatting fails with an {@link java.util.IllegalFormatException} it will return the unformatted message.
     *
     * @param key  The key.
     * @param args The format arguments
     * @return The message
     */
    @NotNull
    public Component getMessage(@NotNull String key, @NotNull Object[] args) {
        String plainText = getMessage0(key);
        try {
            plainText = plainText.formatted(args);
        } catch (IllegalFormatException exc) {
            exc.printStackTrace();
        }

        return MiniMessage.miniMessage().deserialize(plainText);
    }

    /**
     * Get the language key
     *
     * @return The language
     */
    @NotNull
    public Locale getLocale() {
        return locale;
    }

    /**
     * Get the item which represents this language
     *
     * @return The item
     */
    @NotNull
    public ItemStack getItem() {
        return item.clone();
    }

    /**
     * Get the default language
     *
     * @return The default language
     */
    public Optional<Language> getFallbackLanguage() {
        return Optional.ofNullable(fallbackLanguage);
    }

    /**
     * Set the default language.
     * If the default language is the same as this language or has cyclic dependency it will set to null
     *
     * @param fallbackLanguage The new default language
     */
    public void setFallbackLanguage(@Nullable Language fallbackLanguage) {
        if (fallbackLanguage == null || fallbackLanguage == this) {
            this.fallbackLanguage = null;
            return;
        }

        //check for cycles in fallback graph
        Optional<Language> tmp = fallbackLanguage.getFallbackLanguage();
        while (tmp.isPresent()) {
            if (tmp.get() == this) {
                this.fallbackLanguage = null;
            }
            tmp = tmp.get().getFallbackLanguage();
        }
        this.fallbackLanguage = fallbackLanguage;
    }


    /**
     * Load the item from the config or set the texture to the default {@link Items#MISSING_TEXTURE} value.
     */
    private void loadItem() {
        item = new SkullBuilder()
                .setTexture(config.getString("flag", Items.MISSING_TEXTURE))
                .setDisplayName(Component.text(locale.getDisplayName()))
                .build();
    }

    /**
     * Add the messages from another instance with the same language to this object.
     * Throws an {@link IllegalArgumentException} when the language of the locals is not the same.
     *
     * @param other            Other language instance
     * @param overrideExisting Should it override already existing keys? (including the item texture)
     */
    public void addLanguageKeys(Language other, boolean overrideExisting) {
        if (!locale.equals(other.locale))
            throw new IllegalArgumentException("Local must be of type %s but is %s".formatted(locale, other.locale));
        this.config.addAll(other.config, overrideExisting);
        if (overrideExisting && other.config.contains("flag")) loadItem();
    }
}
