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

package me.skulduggerry.utilities.config;

import me.skulduggerry.utilities.utils.ReflectionUtils;
import me.skulduggerry.utilities.version.Version;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a source of configurable options and settings
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public interface Config {

    /**
     * Loads a config from the given reader.
     * The implementation of config must support this kind of instantiation or a {@link RuntimeException} will be thrown.
     *
     * @param reader The reader to load the config from.
     * @param type   The implementation of config.
     * @return The loaded config.
     */
    @NotNull
    static Config load(@NotNull Reader reader, @NotNull Class<? extends Config> type) {
        return ReflectionUtils.createNewInstance(type, reader).orElseThrow(() -> new RuntimeException("The given config does not support the creation via Reader!"));
    }

    /**
     * Gets the requested Object by path.
     * <p>
     * If the Object does not exist this will return null.
     *
     * @param path Path of the Object to get.
     * @return Requested Object.
     */
    @Nullable
    Object get(@NotNull String path);

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     *
     * @param path Path of the Object to get.
     * @param def  The default value to return if the path is not found.
     * @return Requested Object.
     */
    @NotNull
    Object get(@NotNull String path, @NotNull Object def);

    /**
     * Checks if the specified path is a boolean.
     * <p>
     * If the path exists but is not a boolean, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the boolean to check.
     * @return Whether the specified path is a boolean.
     */
    boolean isBoolean(@NotNull String path);

    /**
     * Gets the requested boolean by path.
     * <p>
     * If the boolean does not exist this will return false.
     *
     * @param path Path of the boolean to get.
     * @return Requested boolean.
     */
    boolean getBoolean(@NotNull String path);

    /**
     * Gets the requested boolean by path, returning a default value if not
     * found.
     *
     * @param path Path of the boolean to get.
     * @param def  The default value to return if the path is not found or is
     *             not a boolean.
     * @return Requested boolean.
     */
    boolean getBoolean(@NotNull String path, boolean def);

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
    @NotNull
    List<Boolean> getBooleanList(@NotNull String path);

    /**
     * Checks if the specified path is a byte.
     * <p>
     * If the path exists but is not a byte, this will return false.
     * If the path does not exist, this will return false.
     *
     * @param path Path of the int to check.
     * @return Whether the specified path is a byte.
     */
    boolean isByte(@NotNull String path);

    /**
     * Gets the requested byte by path.
     * <p>
     * If the byte does not exist this will return 0.
     *
     * @param path Path of the byte to get.
     * @return Requested byte.
     */
    byte getByte(@NotNull String path);

    /**
     * Gets the requested byte by path, returning a default value if not found.
     *
     * @param path Path of the byte to get.
     * @param def  The default value to return if the path is not found or is
     *             not a byte.
     * @return Requested byte.
     */
    byte getByte(@NotNull String path, byte def);

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
    @NotNull
    List<Byte> getByteList(@NotNull String path);

    /**
     * Checks if the specified path is a short.
     * <p>
     * If the path exists but is not a byte, this will return false.
     * If the path does not exist, this will return false.
     *
     * @param path Path of the short to check.
     * @return Whether the specified path is a short.
     */
    boolean isShort(@NotNull String path);

    /**
     * Gets the requested short by path.
     * <p>
     * If the short does not exist this will return 0.
     *
     * @param path Path of the short to get.
     * @return Requested short.
     */
    short getShort(@NotNull String path);

    /**
     * Gets the requested short by path, returning a default value if not found.
     *
     * @param path Path of the short to get.
     * @param def  The default value to return if the path is not found or is
     *             not a short.
     * @return Requested byte.
     */
    short getShort(@NotNull String path, short def);

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
    @NotNull
    List<Short> getShortList(@NotNull String path);

    /**
     * Checks if the specified path is an int.
     * <p>
     * If the path exists but is not an int, this will return false.
     * If the path does not exist, this will return false.
     *
     * @param path Path of the int to check.
     * @return Whether the specified path is an int.
     */
    boolean isInt(@NotNull String path);

    /**
     * Gets the requested int by path.
     * <p>
     * If the int does not exist this will return 0.
     *
     * @param path Path of the int to get.
     * @return Requested int.
     */
    int getInt(@NotNull String path);

    /**
     * Gets the requested int by path, returning a default value if not found.
     *
     * @param path Path of the int to get.
     * @param def  The default value to return if the path is not found or is
     *             not an int.
     * @return Requested int.
     */
    int getInt(@NotNull String path, int def);

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
    @NotNull
    List<Integer> getIntegerList(@NotNull String path);

    /**
     * Checks if the specified path is a long.
     * <p>
     * If the path exists but is not a long, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the long to check.
     * @return Whether the specified path is a long.
     */
    boolean isLong(@NotNull String path);

    /**
     * Gets the requested long by path.
     * <p>
     * If the long does not exist this will return 0.
     *
     * @param path Path of the long to get.
     * @return Requested long.
     */
    long getLong(@NotNull String path);

    /**
     * Gets the requested long by path, returning a default value if not found.
     *
     * @param path Path of the long to get.
     * @param def  The default value to return if the path is not found or is
     *             not a long.
     * @return Requested long.
     */
    long getLong(@NotNull String path, long def);

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
    @NotNull
    List<Long> getLongList(@NotNull String path);

    /**
     * Checks if the specified path is a float.
     * <p>
     * If the path exists but is not a float, this will return false.
     * If the path does not exist, this will return false.
     *
     * @param path Path of the float to check.
     * @return Whether the specified path is a float.
     */
    boolean isFloat(@NotNull String path);

    /**
     * Gets the requested float by path.
     * <p>
     * If the float does not exist this will return 0.
     *
     * @param path Path of the float to get.
     * @return Requested float.
     */
    float getFloat(@NotNull String path);

    /**
     * Gets the requested float by path, returning a default value if not found.
     *
     * @param path Path of the float to get.
     * @param def  The default value to return if the path is not found or is
     *             not a float.
     * @return Requested float.
     */
    float getFloat(@NotNull String path, float def);

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
    @NotNull
    List<Float> getFloatList(@NotNull String path);

    /**
     * Checks if the specified path is a double.
     * <p>
     * If the path exists but is not a double, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the double to check.
     * @return Whether the specified path is a double.
     */
    boolean isDouble(@NotNull String path);

    /**
     * Gets the requested double by path.
     * <p>
     * If the double does not exist this will return 0.
     *
     * @param path Path of the double to get.
     * @return Requested double.
     */
    double getDouble(@NotNull String path);

    /**
     * Gets the requested double by path, returning a default value if not found.
     *
     * @param path Path of the double to get.
     * @param def  The default value to return if the path is not found or is
     *             not a double.
     * @return Requested double.
     */
    double getDouble(@NotNull String path, double def);

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
    @NotNull
    List<Double> getDoubleList(@NotNull String path);

    /**
     * Checks if the specified path is a char.
     * <p>
     * If the path exists but is not a char, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the char to check.
     * @return Whether the specified path is a char.
     */
    boolean isCharacter(@NotNull String path);

    /**
     * Gets the requested char by path.
     * <p>
     * If the char does not exist this will return 0.
     *
     * @param path Path of the char to get.
     * @return Requested char.
     */
    char getCharacter(@NotNull String path);

    /**
     * Gets the requested char by path, returning a default value if not found.
     *
     * @param path Path of the char to get.
     * @param def  The default value to return if the path is not found or is
     *             not a char.
     * @return Requested char.
     */
    char getCharacter(@NotNull String path, char def);

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
    @NotNull
    List<Character> getCharacterList(@NotNull String path);

    /**
     * Checks if the specified path is a String.
     * <p>
     * If the path exists but is not a String, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the String to check.
     * @return Whether the specified path is a String.
     */
    boolean isString(@NotNull String path);

    /**
     * Gets the requested String by path.
     * <p>
     * If the String does not exist this will return null.
     *
     * @param path Path of the String to get.
     * @return Requested String.
     */
    @Nullable
    String getString(@NotNull String path);

    /**
     * Gets the requested String by path, returning a default value if not
     * found.
     *
     * @param path Path of the String to get.
     * @param def  The default value to return if the path is not found or is
     *             not a String.
     * @return Requested String.
     */
    @NotNull
    String getString(@NotNull String path, @NotNull String def);

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
    @NotNull
    List<String> getStringList(@NotNull String path);

    /**
     * Checks if the specified path is a Version.
     * <p>
     * If the path exists but is not a Version, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the Version to check.
     * @return Whether the specified path is a Version.
     */
    boolean isVersion(@NotNull String path);

    /**
     * Gets the requested Version by path.
     * <p>
     * If the Version does not exist this will return null.
     *
     * @param path Path of the Version to get.
     * @return Requested Version.
     */
    @Nullable
    Version getVersion(@NotNull String path);

    /**
     * Gets the requested {@link Version} by path, returning a default value if
     * not found.
     *
     * @param path Path of the Version to get.
     * @param def  The default value to return if the path is not found or is
     *             not a Version.
     * @return Requested Version.
     */
    @NotNull
    Version getVersion(@NotNull String path, @NotNull Version def);

    /**
     * Checks if the specified path is a Color.
     * <p>
     * If the path exists but is not a Color, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the Color to check.
     * @return Whether the specified path is a Color.
     */
    boolean isColor(@NotNull String path);

    /**
     * Gets the requested Color by path.
     * <p>
     * If the Color does not exist this will return null.
     *
     * @param path Path of the Color to get.
     * @return Requested Color.
     */
    @Nullable
    Color getColor(@NotNull String path);

    /**
     * Gets the requested {@link Color} by path, returning a default value if
     * not found.
     *
     * @param path Path of the Color to get.
     * @param def  The default value to return if the path is not found or is
     *             not a Color.
     * @return Requested Color.
     */
    @NotNull
    Color getColor(@NotNull String path, @NotNull Color def);

    /**
     * Checks if the specified path is an ItemStack.
     * <p>
     * If the path exists but is not a ItemStack, this will return false. If
     * the path does not exist, this will return false.
     *
     * @param path Path of the ItemStack to check.
     * @return Whether the specified path is an ItemStack.
     */
    boolean isItemStack(@NotNull String path);

    /**
     * Gets the requested ItemStack by path.
     * <p>
     * If the ItemStack does not exist this will return null.
     *
     * @param path Path of the ItemStack to get.
     * @return Requested ItemStack.
     */
    @Nullable
    ItemStack getItemStack(@NotNull String path);

    /**
     * Gets the requested {@link ItemStack} by path, returning a default value
     * if not found.
     *
     * @param path Path of the ItemStack to get.
     * @param def  The default value to return if the path is not found or is
     *             not an ItemStack.
     * @return Requested ItemStack.
     */
    @NotNull
    ItemStack getItemStack(@NotNull String path, @NotNull ItemStack def);

    /**
     * Checks if the specified path is a Location.
     * <p>
     * If the path exists but is not a Location, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the Location to check.
     * @return Whether the specified path is a Location.
     */
    boolean isLocation(@NotNull String path);

    /**
     * Gets the requested Location by path.
     * <p>
     * If the Location does not exist this will return null.
     *
     * @param path Path of the Location to get.
     * @return Requested Location.
     */
    @Nullable
    Location getLocation(@NotNull String path);

    /**
     * Gets the requested {@link Location} by path, returning a default value if
     * not found.
     *
     * @param path Path of the Location to get.
     * @param def  The default value to return if the path is not found or is not
     *             a Location.
     * @return Requested Location.
     */
    @NotNull
    Location getLocation(@NotNull String path, @NotNull Location def);

    /**
     * Gets the requested {@link ConfigurationSerializable} object at the given path.
     * <p>
     * If the Object does not exist this will return null.
     *
     * @param <T>  the type of {@link ConfigurationSerializable}
     * @param path the path to the object.
     * @param type the type of {@link ConfigurationSerializable}
     * @return Requested {@link ConfigurationSerializable} object
     */
    @Nullable <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> type);

    /**
     * Gets the requested {@link ConfigurationSerializable} object at the given
     * path, returning a default value if not found
     *
     * @param <T>  the type of {@link ConfigurationSerializable}
     * @param path the path to the object.
     * @param def  the default object to return if the object is not present at
     *             the path
     * @return Requested {@link ConfigurationSerializable} object
     */
    @NotNull <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull T def);

    /**
     * Gets the requested {@link Enum} object at the given path.
     * <p>
     * If the Object does not exist this will return null.
     *
     * @param <T>  the type of {@link Enum}
     * @param path the path to the object.
     * @param type the type of {@link Enum}
     * @return Requested {@link Enum} object
     */
    @Nullable <T extends Enum<T>> T getEnum(@NotNull String path, @NotNull Class<T> type);

    /**
     * Gets the requested {@link Enum} object at the given
     * path, returning a default value if not found
     *
     * @param <T>  the type of {@link Enum}
     * @param path the path to the object.
     * @param def  the default object to return if the object is not present at
     *             the path
     * @return Requested {@link Enum} object
     */
    @NotNull <T extends Enum<T>> T getEnum(@NotNull String path, @NotNull T def);

    /**
     * Gets the requested child config by path.
     * <p>
     * If the child config does not exist this will create a new config.
     *
     * @param path Path of the child config to get.
     * @return Requested child config.
     */
    @NotNull
    Config getConfig(@NotNull String path);

    /**
     * Gets the root {@link Config} that contains this {@link Config}
     * <p>
     * If this {@link Config} is the root it will return itself.
     *
     * @return Root {@link Config} containing this {@link Config}.
     */
    @NotNull
    Config getRoot();

    /**
     * Gets the parent {@link Config} that directly contains
     * this {@link Config}.
     * <p>
     * If this {@link Config} is the root it will return null.
     *
     * @return Parent {@link Config} containing this {@link Config}.
     */
    @NotNull
    Optional<Config> getParent();

    /**
     * Checks if this {@link Config} contains the given path.
     *
     * @param path Path to check for existence.
     * @return True if this section contains the requested path.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    boolean contains(@NotNull String path);

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
    void set(@NotNull String path, @Nullable Object value);

    /**
     * Adds all values from another {@link Config} to this one.
     *
     * @param config               The config which values should be added to this one.
     * @param overrideExistingKeys Whether to override existing keys.
     */
    void addAll(@NotNull Config config, boolean overrideExistingKeys);

    /**
     * Removes an object at the given path from the config.
     *
     * @param path Path to remove.
     */
    void remove(@NotNull String path);

    /**
     * Removes all paths in this config.
     */
    void clear();

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
    @NotNull Set<String> children();

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
    @NotNull Map<String, Object> values();

    /**
     * Get the size of the config.
     *
     * @return The size.
     */
    @Range(from = 0, to = Integer.MAX_VALUE)
    int size();

    /**
     * Checks if the config is empty.
     *
     * @return Whether the config is empty.
     */
    boolean isEmpty();

    /**
     * Checks if the config is read-only.
     *
     * @return Whether the config is read-only.
     */
    boolean isReadOnly();

    /**
     * Returns a not modifiable config.
     * <p>
     * If this config is already read only it will return itself.
     *
     * @return The read only config.
     */
    @UnmodifiableView
    @NotNull Config readOnly();

    /**
     * Saves this {@link Config} to a string, and returns it.
     *
     * @return String containing this config.
     */
    @NotNull
    String saveToString();

    /**
     * Saves this {@link Config} to the writer
     *
     * @param writer Writer to save to.
     * @throws IOException Thrown when the given writer cannot be written
     *                     for any reason.
     */
    void save(@NotNull Writer writer) throws IOException;
}

