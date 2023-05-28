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

package me.skulduggerry.utilities.config.abstraction;

import me.skulduggerry.utilities.config.Config;
import me.skulduggerry.utilities.config.wrapper.ReadOnlyWrapper;
import me.skulduggerry.utilities.utils.SerializationUtils;
import me.skulduggerry.utilities.utils.VersionUtils;
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
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Represents a source of configurable options and settings
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public abstract class AbstractConfig implements Config {

    private final Config ROOT, PARENT;

    protected AbstractConfig(@NotNull Config root,@NotNull Config parent) {
        this.ROOT = root;
        this.PARENT = parent;
    }

    /**
     * Constructor
     * <p>
     * Creates an empty {@link Config} with no default values.
     */
    public AbstractConfig() {
        ROOT = this;
        PARENT = null;
    }

    /**
     * Get the requested Object from the {@link Supplier}, returning the default value
     * if the getter returns null.
     *
     * @param getter The function to get the requested object.
     * @param def    The default value.
     * @param <T>    The type of the requested object.
     * @return Requested Object.
     */
    @NotNull
    private <T> T getDefault(@NotNull Supplier<T> getter, @NotNull T def) {
        T result = getter.get();
        return result != null ? result : def;
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
        return getDefault(() -> get(path), def);
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
        return getBoolean(path, false);
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
        return getByte(path, (byte) 0);
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
        return getShort(path, (short) 0);
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
        return getInt(path, 0);
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
        return getLong(path, 0);
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
        return getFloat(path, 0.0F);
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
        return getDouble(path, 0.0D);
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
        return isString(path);
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
        return getCharacter(path, (char) 0);
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
        try {
            return getString(path).charAt(0);
        } catch (Throwable e) {
            return def;
        }
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
        return getDefault(() -> getString(path), def);
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
        return getVersion(path) != null;
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
        String vString = getString(path);
        return VersionUtils.parse(vString);
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
        return getDefault(() -> getVersion(path), def);
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
        return getColor(path) != null;
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
        return getSerializable(path, Color.class);
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
        return getDefault(() -> getColor(path), def);
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
        return getItemStack(path) != null;
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
        return getSerializable(path, ItemStack.class);
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
        return getDefault(() -> getItemStack(path), def);
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
        return getLocation(path) != null;
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
        return getSerializable(path, Location.class);
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
        return getDefault(() -> getLocation(path), def);
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
        return SerializationUtils.deserialize(getConfig(path).values(), type);
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
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ConfigurationSerializable> @NotNull T getSerializable(@NotNull String path, @NotNull T def) {
        return (T) getDefault(() -> getSerializable(path, def.getClass()), def);
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
        String name = getString(path);
        if (name == null) return null;

        try {
            return Enum.valueOf(type, name);
        } catch (IllegalArgumentException e) {
            return null;
        }
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
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Enum<T>> @NotNull T getEnum(@NotNull String path, @NotNull T def) {
        return (T) getDefault(() -> getEnum(path, def.getClass()), def);
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
        return ROOT;
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
        return Optional.ofNullable(PARENT);
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
    public final void set(@NotNull String path, @Nullable Object value) {
        if (value instanceof Enum<?> enum_) {
            value = enum_.name();
        } else if (value instanceof Version version) {
            value = version.toString();
        }
        set0(path, value);
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
    protected abstract void set0(@NotNull String path, @Nullable Object value);

    /**
     * Adds all values from another {@link Config} to this one.
     *
     * @param config               The config which values should be added to this one.
     * @param overrideExistingKeys Whether to override existing keys.
     */
    @Override
    public void addAll(@NotNull Config config, boolean overrideExistingKeys) {
        config.values().forEach((key, value) -> {
            if (overrideExistingKeys || !this.contains(key))
                this.set(key, value);
        });
    }

    /**
     * Removes an object at the given path from the config.
     *
     * @param path Path to remove.
     */
    @Override
    public void remove(@NotNull String path) {
        set(path, null);
    }

    /**
     * Removes all paths in this config.
     */
    @Override
    public void clear() {
        children().forEach(this::remove);
    }

    /**
     * Get the size of the config.
     *
     * @return The size.
     */
    @Override
    @Range(from = 0, to = Integer.MAX_VALUE)
    public int size() {
        return children().size();
    }

    /**
     * Checks if the config is empty.
     *
     * @return Whether the config is empty.
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Checks if the config is read-only.
     *
     * @return Whether the config is read-only.
     */
    @Override
    public boolean isReadOnly() {
        return false;
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
        return ReadOnlyWrapper.asReadOnlyConfig(this);
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
        writer.write(this.saveToString());
    }
}
