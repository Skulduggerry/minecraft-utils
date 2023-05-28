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

package me.skulduggerry.utilities.config.type;

import me.skulduggerry.utilities.config.Config;
import me.skulduggerry.utilities.config.abstraction.AbstractConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.yaml.snakeyaml.DumperOptions.FlowStyle;

/**
 * Config for yaml configurations.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class YamlConfig extends AbstractConfig {

    private final ConfigurationSection config;

    /**
     * Constructor.
     * <p>
     * Creates an empty {@link Config} with no default values.
     */
    public YamlConfig() {
        this.config = new YamlConfiguration();
    }

    /**
     * Constructor.
     * <p>
     * Creates a config from an existing source.
     *
     * @param reader The source of the config.
     */
    public YamlConfig(@NotNull Reader reader) {
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(reader);
            this.config = config;
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor.
     * <p>
     * Creates a child config with the given root, parent and config.
     *
     * @param root   The root config.
     * @param parent The parent config.
     * @param config The underlying config.
     */
    private YamlConfig(@NotNull Config root, @NotNull Config parent, @NotNull ConfigurationSection config) {
        super(root, parent);
        this.config = config;
    }

    /**
     * Gets the requested Object by path.
     * <p>
     * If the Object does not exist this will return null.
     *
     * @param path Path of the Object to get.
     * @return Requested Object.
     */
    @Override
    public @Nullable Object get(@NotNull String path) {
        return config.get(path);
    }

    /**
     * Checks if the specified path is a boolean.
     * <p>
     * If the path exists but is not a boolean, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the boolean to check.
     * @return Whether the specified path is a boolean.
     */
    @Override
    public boolean isBoolean(@NotNull String path) {
        return config.isBoolean(path);
    }

    /**
     * Gets the requested boolean by path, returning a default value if not
     * found.
     *
     * @param path Path of the boolean to get.
     * @param def  The default value to return if the path is not found or is
     *             not a boolean.
     * @return Requested boolean.
     */
    @Override
    public boolean getBoolean(@NotNull String path, boolean def) {
        return config.getBoolean(path, def);
    }

    /**
     * Gets the requested List of boolean by path.
     * <p>
     * If the List does not exist this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a boolean if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of boolean.
     */
    @Override
    public @NotNull List<Boolean> getBooleanList(@NotNull String path) {
        return config.getBooleanList(path);
    }

    /**
     * Checks if the specified path is a byte.
     * <p>
     * If the path exists but is not a byte, this will return false.
     * If the path does not exist, this will return false.
     *
     * @param path Path of the int to check.
     * @return Whether the specified path is a byte.
     */
    @Override
    public boolean isByte(@NotNull String path) {
        return config.isInt(path);
    }

    /**
     * Gets the requested byte by path, returning a default value if not found.
     *
     * @param path Path of the byte to get.
     * @param def  The default value to return if the path is not found or is
     *             not a byte.
     * @return Requested byte.
     */
    @Override
    public byte getByte(@NotNull String path, byte def) {
        if (!isByte(path)) return def;
        return (byte) getInt(path);
    }

    /**
     * Gets the requested List of byte by path.
     * <p>
     * If the List does not exist this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a byte if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of byte.
     */
    @Override
    public @NotNull List<Byte> getByteList(@NotNull String path) {
        return config.getByteList(path);
    }

    /**
     * Checks if the specified path is a short.
     * <p>
     * If the path exists but is not a byte, this will return false.
     * If the path does not exist, this will return false.
     *
     * @param path Path of the short to check.
     * @return Whether the specified path is a short.
     */
    @Override
    public boolean isShort(@NotNull String path) {
        return config.isInt(path);
    }

    /**
     * Gets the requested short by path, returning a default value if not found.
     *
     * @param path Path of the short to get.
     * @param def  The default value to return if the path is not found or is
     *             not a short.
     * @return Requested byte.
     */
    @Override
    public short getShort(@NotNull String path, short def) {
        if (!isShort(path)) return def;
        return (short) getInt(path);
    }

    /**
     * Gets the requested List of byte by path.
     * <p>
     * If the List does not exist this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a byte if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of short.
     */
    @Override
    public @NotNull List<Short> getShortList(@NotNull String path) {
        return config.getShortList(path);
    }

    /**
     * Checks if the specified path is an int.
     * <p>
     * If the path exists but is not an int, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the int to check.
     * @return Whether the specified path is an int.
     */
    @Override
    public boolean isInt(@NotNull String path) {
        return config.isInt(path);
    }

    /**
     * Gets the requested int by path, returning a default value if not found.
     *
     * @param path Path of the int to get.
     * @param def  The default value to return if the path is not found or is
     *             not an int.
     * @return Requested int.
     */
    @Override
    public int getInt(@NotNull String path, int def) {
        return config.getInt(path, def);
    }

    /**
     * Gets the requested List of int by path.
     * <p>
     * If the List does not exist this will return an empty List.
     * <p>
     * This method will attempt to cast any values into an int if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of int.
     */
    @Override
    public @NotNull List<Integer> getIntegerList(@NotNull String path) {
        return config.getIntegerList(path);
    }

    /**
     * Checks if the specified path is a long.
     * <p>
     * If the path exists but is not a long, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the long to check.
     * @return Whether the specified path is a long.
     */
    @Override
    public boolean isLong(@NotNull String path) {
        return config.isLong(path);
    }

    /**
     * Gets the requested long by path, returning a default value if not found.
     *
     * @param path Path of the long to get.
     * @param def  The default value to return if the path is not found or is
     *             not a long.
     * @return Requested long.
     */
    @Override
    public long getLong(@NotNull String path, long def) {
        return config.getLong(path, def);
    }

    /**
     * Gets the requested List of long by path.
     * <p>
     * If the List does not exist this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a long if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of long.
     */
    @Override
    public @NotNull List<Long> getLongList(@NotNull String path) {
        return config.getLongList(path);
    }

    /**
     * Checks if the specified path is a float.
     * <p>
     * If the path exists but is not a float, this will return false.
     * If the path does not exist, this will return false.
     *
     * @param path Path of the float to check.
     * @return Whether the specified path is a float.
     */
    @Override
    public boolean isFloat(@NotNull String path) {
        return config.isDouble(path);
    }

    /**
     * Gets the requested float by path, returning a default value if not found.
     *
     * @param path Path of the float to get.
     * @param def  The default value to return if the path is not found or is
     *             not a float.
     * @return Requested float.
     */
    @Override
    public float getFloat(@NotNull String path, float def) {
        if (!isFloat(path)) return def;
        return (float) getDouble(path);
    }

    /**
     * Gets the requested List of float by path.
     * <p>
     * If the List does not exist this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a float if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of float.
     */
    @Override
    public @NotNull List<Float> getFloatList(@NotNull String path) {
        return config.getFloatList(path);
    }

    /**
     * Checks if the specified path is a double.
     * <p>
     * If the path exists but is not a double, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the double to check.
     * @return Whether the specified path is a double.
     */
    @Override
    public boolean isDouble(@NotNull String path) {
        return config.isDouble(path);
    }

    /**
     * Gets the requested double by path, returning a default value if not found.
     *
     * @param path Path of the double to get.
     * @param def  The default value to return if the path is not found or is
     *             not a double.
     * @return Requested double.
     */
    @Override
    public double getDouble(@NotNull String path, double def) {
        return config.getDouble(path, def);
    }

    /**
     * Gets the requested List of double by path.
     * <p>
     * If the List does not exist this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a double if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of double.
     */
    @Override
    public @NotNull List<Double> getDoubleList(@NotNull String path) {
        return config.getDoubleList(path);
    }

    /**
     * Gets the requested List of char by path.
     * <p>
     * If the List does not exist this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a char if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of char.
     */
    @Override
    public @NotNull List<Character> getCharacterList(@NotNull String path) {
        return config.getCharacterList(path);
    }

    /**
     * Checks if the specified path is a String.
     * <p>
     * If the path exists but is not a String, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the String to check.
     * @return Whether the specified path is a String.
     */
    @Override
    public boolean isString(@NotNull String path) {
        return config.isString(path);
    }

    /**
     * Gets the requested String by path.
     * <p>
     * If the String does not exist this will return null.
     *
     * @param path Path of the String to get.
     * @return Requested String.
     */
    @Override
    public @Nullable String getString(@NotNull String path) {
        return config.getString(path);
    }

    /**
     * Gets the requested List of String by path.
     * <p>
     * If the List does not exist this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a String if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of String.
     */
    @Override
    public @NotNull List<String> getStringList(@NotNull String path) {
        return config.getStringList(path);
    }

    /**
     * Gets the requested {@link ConfigurationSerializable} object at the given path.
     * <p>
     * If the Object does not exist this will return null.
     *
     * @param path the path to the object.
     * @param type the type of {@link ConfigurationSerializable}
     * @return Requested {@link ConfigurationSerializable} object
     */
    @Override
    public <T extends ConfigurationSerializable> @Nullable T getSerializable(@NotNull String path, @NotNull Class<T> type) {
        return config.getSerializable(path, type);
    }

    /**
     * Gets the requested child config by path.
     * <p>
     * If the child config does not exist this will create a new config.
     *
     * @param path Path of the child config to get.
     * @return Requested child config.
     */
    @Override
    public @NotNull Config getConfig(@NotNull String path) {
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            section = config.createSection(path);
        }
        return new YamlConfig(getRoot(), this, section);
    }

    /**
     * Checks if this {@link Config} contains the given path.
     *
     * @param path Path to check for existence.
     * @return True if this section contains the requested path.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    @Override
    public boolean contains(@NotNull String path) {
        return config.contains(path);
    }

    /**
     * Gets a set containing all keys in this section.
     * <p>
     * If deep is set to true, then this will contain all the keys within any
     * child {@link Config}s (and their children, etc.). These
     * will be in a valid path notation for you to use.
     * <p>
     * If deep is set to false, then this will contain only the keys of any
     * direct children, and not their own children.
     *
     * @return Set of keys contained within this config.
     */
    @Override
    public @NotNull Set<String> children() {
        return config.getKeys(true);
    }

    /**
     * Gets a Map containing all keys and their values for this section.
     * <p>
     * If deep is set to true, then this will contain all the keys and values
     * within any child {@link Config}s (and their children, etc.).
     * These keys will be in a valid path notation for you to use.
     * <p>
     * If deep is set to false, then this will contain only the keys and
     * values of any direct children, and not their own children.
     *
     * @return Map of keys and values of this config.
     */
    @Override
    public @NotNull Map<String, Object> values() {
        return config.getValues(true);
    }

    /**
     * Saves this {@link Config} to a string, and returns it.
     *
     * @return String containing this config.
     */
    @Override
    public @NotNull String saveToString() {
        DumperOptions yamlOptions = new DumperOptions();
        LoaderOptions loaderOptions = new LoaderOptions();
        Representer yamlRepresenter = new YamlRepresenter();
        Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions, loaderOptions);

        yamlOptions.setIndent(2);
        yamlOptions.setDefaultFlowStyle(FlowStyle.BLOCK);
        yamlRepresenter.setDefaultFlowStyle(FlowStyle.BLOCK);
        String dump = yaml.dump(config.getValues(false));
        if (dump.equals("{}\n")) {
            dump = "";
        }

        return dump;
    }

    /**
     * Sets the specified path to the given value.
     * <p>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless of what the new value is.
     * <p>
     * Some implementations may have limitations on what you may store. See
     * their individual javadocs for details. No implementations should allow
     * you to store {@link Config}s, please use
     * {@link #getConfig(String)} for that.
     *
     * @param path  Path of the object to set.
     * @param value New value to set the path to.
     */
    @Override
    protected void set0(@NotNull String path, @Nullable Object value) {
        config.set(path, value);
    }
}

