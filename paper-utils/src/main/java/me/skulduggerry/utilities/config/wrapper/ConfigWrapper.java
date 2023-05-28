package me.skulduggerry.utilities.config.wrapper;

import me.skulduggerry.utilities.config.Config;
import me.skulduggerry.utilities.version.Version;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Wrapper for configs.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class ConfigWrapper implements Config {

    private final Config wrapped;

    public ConfigWrapper(@NotNull Config wrapped) {
        this.wrapped = wrapped;
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
        return getWrappedConfig().get(path);
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     *
     * @param path Path of the Object to get.
     * @param def  The default value to return if the path is not found.
     * @return Requested Object.
     */
    @Override
    public @NotNull Object get(@NotNull String path, @NotNull Object def) {
        return getWrappedConfig().get(path, def);
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
        return getWrappedConfig().isBoolean(path);
    }

    /**
     * Gets the requested boolean by path.
     * <p>
     * If the boolean does not exist this will return false.
     *
     * @param path Path of the boolean to get.
     * @return Requested boolean.
     */
    @Override
    public boolean getBoolean(@NotNull String path) {
        return getWrappedConfig().getBoolean(path);
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
        return getWrappedConfig().getBoolean(path, def);
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
        return getWrappedConfig().getBooleanList(path);
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
        return getWrappedConfig().isByte(path);
    }

    /**
     * Gets the requested byte by path.
     * <p>
     * If the byte does not exist this will return 0.
     *
     * @param path Path of the byte to get.
     * @return Requested byte.
     */
    @Override
    public byte getByte(@NotNull String path) {
        return getWrappedConfig().getByte(path);
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
        return getWrappedConfig().getByte(path, def);
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
        return getWrappedConfig().getByteList(path);
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
        return getWrappedConfig().isShort(path);
    }

    /**
     * Gets the requested short by path.
     * <p>
     * If the short does not exist this will return 0.
     *
     * @param path Path of the short to get.
     * @return Requested short.
     */
    @Override
    public short getShort(@NotNull String path) {
        return getWrappedConfig().getShort(path);
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
        return getWrappedConfig().getShort(path, def);
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
        return getWrappedConfig().getShortList(path);
    }

    /**
     * Checks if the specified path is an int.
     * <p>
     * If the path exists but is not an int, this will return false.
     * If the path does not exist, this will return false.
     *
     * @param path Path of the int to check.
     * @return Whether the specified path is an int.
     */
    @Override
    public boolean isInt(@NotNull String path) {
        return getWrappedConfig().isInt(path);
    }

    /**
     * Gets the requested int by path.
     * <p>
     * If the int does not exist this will return 0.
     *
     * @param path Path of the int to get.
     * @return Requested int.
     */
    @Override
    public int getInt(@NotNull String path) {
        return getWrappedConfig().getInt(path);
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
        return getWrappedConfig().getInt(path, def);
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
        return getWrappedConfig().getIntegerList(path);
    }

    /**
     * Checks if the specified path is a long.
     * <p>
     * If the path exists but is not a long, this will return false.
     * If the path does not exist, this will return false.
     *
     * @param path Path of the long to check.
     * @return Whether the specified path is a long.
     */
    @Override
    public boolean isLong(@NotNull String path) {
        return getWrappedConfig().isLong(path);
    }

    /**
     * Gets the requested long by path.
     * <p>
     * If the long does not exist this will return 0.
     *
     * @param path Path of the long to get.
     * @return Requested long.
     */
    @Override
    public long getLong(@NotNull String path) {
        return getWrappedConfig().getLong(path);
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
        return getWrappedConfig().getLong(path, def);
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
        return getWrappedConfig().getLongList(path);
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
        return getWrappedConfig().isFloat(path);
    }

    /**
     * Gets the requested float by path.
     * <p>
     * If the float does not exist this will return 0.
     *
     * @param path Path of the float to get.
     * @return Requested float.
     */
    @Override
    public float getFloat(@NotNull String path) {
        return getWrappedConfig().getFloat(path);
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
        return getWrappedConfig().getFloat(path, def);
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
        return getWrappedConfig().getFloatList(path);
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
        return getWrappedConfig().isDouble(path);
    }

    /**
     * Gets the requested double by path.
     * <p>
     * If the double does not exist this will return 0.
     *
     * @param path Path of the double to get.
     * @return Requested double.
     */
    @Override
    public double getDouble(@NotNull String path) {
        return getWrappedConfig().getDouble(path);
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
        return getWrappedConfig().getDouble(path, def);
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
        return getWrappedConfig().getDoubleList(path);
    }

    /**
     * Checks if the specified path is a char.
     * <p>
     * If the path exists but is not a char, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the char to check.
     * @return Whether the specified path is a char.
     */
    @Override
    public boolean isCharacter(@NotNull String path) {
        return getWrappedConfig().isCharacter(path);
    }

    /**
     * Gets the requested char by path.
     * <p>
     * If the char does not exist this will return 0.
     *
     * @param path Path of the char to get.
     * @return Requested char.
     */
    @Override
    public char getCharacter(@NotNull String path) {
        return getWrappedConfig().getCharacter(path);
    }

    /**
     * Gets the requested char by path, returning a default value if not found.
     *
     * @param path Path of the char to get.
     * @param def  The default value to return if the path is not found or is
     *             not a char.
     * @return Requested char.
     */
    @Override
    public char getCharacter(@NotNull String path, char def) {
        return getWrappedConfig().getCharacter(path, def);
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
        return getWrappedConfig().getCharacterList(path);
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
        return getWrappedConfig().isString(path);
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
        return getWrappedConfig().getString(path);
    }

    /**
     * Gets the requested String by path, returning a default value if not
     * found.
     *
     * @param path Path of the String to get.
     * @param def  The default value to return if the path is not found or is
     *             not a String.
     * @return Requested String.
     */
    @Override
    public @NotNull String getString(@NotNull String path, @NotNull String def) {
        return getWrappedConfig().getString(path, def);
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
        return getWrappedConfig().getStringList(path);
    }

    /**
     * Checks if the specified path is a Version.
     * <p>
     * If the path exists but is not a Version, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the Version to check.
     * @return Whether the specified path is a Version.
     */
    @Override
    public boolean isVersion(@NotNull String path) {
        return getWrappedConfig().isVersion(path);
    }

    /**
     * Gets the requested Version by path.
     * <p>
     * If the Version does not exist this will return null.
     *
     * @param path Path of the Version to get.
     * @return Requested Version.
     */
    @Override
    public @Nullable Version getVersion(@NotNull String path) {
        return getWrappedConfig().getVersion(path);
    }

    /**
     * Gets the requested {@link Version} by path, returning a default value if
     * not found.
     *
     * @param path Path of the Version to get.
     * @param def  The default value to return if the path is not found or is
     *             not a Version.
     * @return Requested Version.
     */
    @Override
    public @NotNull Version getVersion(@NotNull String path, @NotNull Version def) {
        return getWrappedConfig().getVersion(path, def);
    }

    /**
     * Checks if the specified path is a Color.
     * <p>
     * If the path exists but is not a Color, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the Color to check.
     * @return Whether the specified path is a Color.
     */
    @Override
    public boolean isColor(@NotNull String path) {
        return getWrappedConfig().isColor(path);
    }

    /**
     * Gets the requested Color by path.
     * <p>
     * If the Color does not exist this will return null.
     *
     * @param path Path of the Color to get.
     * @return Requested Color.
     */
    @Override
    public @Nullable Color getColor(@NotNull String path) {
        return getWrappedConfig().getColor(path);
    }

    /**
     * Gets the requested {@link Color} by path, returning a default value if
     * not found.
     *
     * @param path Path of the Color to get.
     * @param def  The default value to return if the path is not found or is
     *             not a Color.
     * @return Requested Color.
     */
    @Override
    public @NotNull Color getColor(@NotNull String path, @NotNull Color def) {
        return getWrappedConfig().getColor(path, def);
    }

    /**
     * Checks if the specified path is an ItemStack.
     * <p>
     * If the path exists but is not a ItemStack, this will return false. If
     * the path does not exist, this will return false.
     *
     * @param path Path of the ItemStack to check.
     * @return Whether the specified path is an ItemStack.
     */
    @Override
    public boolean isItemStack(@NotNull String path) {
        return getWrappedConfig().isItemStack(path);
    }

    /**
     * Gets the requested ItemStack by path.
     * <p>
     * If the ItemStack does not exist this will return null.
     *
     * @param path Path of the ItemStack to get.
     * @return Requested ItemStack.
     */
    @Override
    public @Nullable ItemStack getItemStack(@NotNull String path) {
        return getWrappedConfig().getItemStack(path);
    }

    /**
     * Gets the requested {@link ItemStack} by path, returning a default value
     * if not found.
     *
     * @param path Path of the ItemStack to get.
     * @param def  The default value to return if the path is not found or is
     *             not an ItemStack.
     * @return Requested ItemStack.
     */
    @Override
    public @NotNull ItemStack getItemStack(@NotNull String path, @NotNull ItemStack def) {
        return getWrappedConfig().getItemStack(path, def);
    }

    /**
     * Checks if the specified path is a Location.
     * <p>
     * If the path exists but is not a Location, this will return false. If the
     * path does not exist, this will return false.
     *
     * @param path Path of the Location to check.
     * @return Whether the specified path is a Location.
     */
    @Override
    public boolean isLocation(@NotNull String path) {
        return getWrappedConfig().isLocation(path);
    }

    /**
     * Gets the requested Location by path.
     * <p>
     * If the Location does not exist this will return null.
     *
     * @param path Path of the Location to get.
     * @return Requested Location.
     */
    @Override
    public @Nullable Location getLocation(@NotNull String path) {
        return getWrappedConfig().getLocation(path);
    }

    /**
     * Gets the requested {@link Location} by path, returning a default value if
     * not found.
     *
     * @param path Path of the Location to get.
     * @param def  The default value to return if the path is not found or is not
     *             a Location.
     * @return Requested Location.
     */
    @Override
    public @NotNull Location getLocation(@NotNull String path, @NotNull Location def) {
        return getWrappedConfig().getLocation(path, def);
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
        return getWrappedConfig().getSerializable(path, type);
    }

    /**
     * Gets the requested {@link ConfigurationSerializable} object at the given
     * path, returning a default value if not found
     *
     * @param path the path to the object.
     * @param def  the default object to return if the object is not present at
     *             the path
     * @return Requested {@link ConfigurationSerializable} object
     */
    @Override
    public <T extends ConfigurationSerializable> @NotNull T getSerializable(@NotNull String path, @NotNull T def) {
        return getWrappedConfig().getSerializable(path, def);
    }

    /**
     * Gets the requested {@link Enum} object at the given path.
     * <p>
     * If the Object does not exist this will return null.
     *
     * @param path the path to the object.
     * @param type the type of {@link Enum}
     * @return Requested {@link Enum} object
     */
    @Override
    public <T extends Enum<T>> @Nullable T getEnum(@NotNull String path, @NotNull Class<T> type) {
        return getWrappedConfig().getEnum(path, type);
    }

    /**
     * Gets the requested {@link Enum} object at the given
     * path, returning a default value if not found
     *
     * @param path the path to the object.
     * @param def  the default object to return if the object is not present at
     *             the path
     * @return Requested {@link Enum} object
     */
    @Override
    public <T extends Enum<T>> @NotNull T getEnum(@NotNull String path, @NotNull T def) {
        return getWrappedConfig().getEnum(path, def);
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
        Config config = getWrappedConfig().getConfig(path);
        return new ConfigWrapper(config);
    }

    /**
     * Gets the root {@link Config} that contains this {@link Config}
     * <p>
     * If this {@link Config} is the root it will return itself.
     *
     * @return Root {@link Config} containing this {@link Config}.
     */
    @Override
    public @NotNull Config getRoot() {
        return getWrappedConfig().getRoot();
    }

    /**
     * Gets the parent {@link Config} that directly contains
     * this {@link Config}.
     * <p>
     * If this {@link Config} is the root it will return null.
     *
     * @return Parent {@link Config} containing this {@link Config}.
     */
    @Override
    public @NotNull Optional<Config> getParent() {
        return getWrappedConfig().getParent();
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
        return getWrappedConfig().contains(path);
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
    public void set(@NotNull String path, @Nullable Object value) {
        getWrappedConfig().set(path, value);
    }

    /**
     * Adds all values from another {@link Config} to this one.
     *
     * @param config               The config which values should be added to this one.
     * @param overrideExistingKeys Whether to override existing keys.
     */
    @Override
    public void addAll(@NotNull Config config, boolean overrideExistingKeys) {
        getWrappedConfig().addAll(config, overrideExistingKeys);
    }

    /**
     * Removes an object at the given path from the config.
     *
     * @param path Path to remove.
     */
    @Override
    public void remove(@NotNull String path) {
        getWrappedConfig().remove(path);
    }

    /**
     * Removes all paths in this config.
     */
    @Override
    public void clear() {
        getWrappedConfig().clear();
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
        return getWrappedConfig().children();
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
        return getWrappedConfig().values();
    }

    /**
     * Get the size of the config.
     *
     * @return The size.
     */
    @Override
    @Range(from = 0, to = Integer.MAX_VALUE)
    public int size() {
        return getWrappedConfig().size();
    }

    /**
     * Checks if the config is empty.
     *
     * @return Whether the config is empty.
     */
    @Override
    public boolean isEmpty() {
        return getWrappedConfig().isEmpty();
    }

    /**
     * Checks if the config is read-only.
     *
     * @return Whether the config is read-only.
     */
    @Override
    public boolean isReadOnly() {
        return this.getClass() == ReadOnlyWrapper.class;
    }

    /**
     * Returns a not modifiable config.
     * <p>
     * If this config is already read only it will return itself.
     *
     * @return The read only config.
     */
    @Override
    public @NotNull Config readOnly() {
        return ReadOnlyWrapper.asReadOnlyConfig(getWrappedConfig());
    }

    /**
     * Saves this {@link Config} to a string, and returns it.
     *
     * @return String containing this config.
     */
    @Override
    public @NotNull String saveToString() {
        return getWrappedConfig().saveToString();
    }

    /**
     * Saves this {@link Config} to the writer
     *
     * @param writer Writer to save to.
     * @throws IOException Thrown when the given writer cannot be written
     *                     for any reason.
     */
    @Override
    public void save(@NotNull Writer writer) throws IOException {
        getWrappedConfig().save(writer);
    }

    @NotNull
    protected Config getWrappedConfig() {
        return wrapped;
    }
}
