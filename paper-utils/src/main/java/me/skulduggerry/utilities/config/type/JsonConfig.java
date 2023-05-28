package me.skulduggerry.utilities.config.type;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.*;
import me.skulduggerry.utilities.adapter.ConfigurationSerializableTypeAdapter;
import me.skulduggerry.utilities.adapter.JsonAdapter;
import me.skulduggerry.utilities.config.Config;
import me.skulduggerry.utilities.config.abstraction.AbstractConfig;
import me.skulduggerry.utilities.utils.JsonUtils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.function.Function;

/**
 * Implementation of the config using json.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class JsonConfig extends AbstractConfig {

    private final static Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(JsonAdapter.getFactory(ConfigurationSerializable.class, new ConfigurationSerializableTypeAdapter()))
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    private final JsonObject config;

    /**
     * Constructor.
     * <p>
     * Creates an empty {@link Config} with no default values.
     */
    public JsonConfig() {
        this.config = new JsonObject();
    }

    /**
     * Constructor.
     * <p>
     * Creates a config from an existing source.
     *
     * @param reader The source of the config.
     */
    public JsonConfig(@NotNull Reader reader) {
        try {
            config = new JsonObject();
            if (reader.ready()) {
                for (Map.Entry<String, JsonElement> entry : GSON.fromJson(reader, JsonObject.class).entrySet()) {
                    String key = entry.getKey();
                    Object value = JsonUtils.unpackJson(entry.getValue());
                    set(key, value);
                }
            }
        } catch (IOException e) {
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
    private JsonConfig(@NotNull Config root, @NotNull Config parent, @NotNull JsonObject config) {
        super(root, parent);
        this.config = config;
    }

    /**
     * Checks whether the object is a primitive
     *
     * @param path   Path to the object
     * @param mapper Mapper function to map to the given type
     * @param <T>    Type of the result
     * @return Result of the check
     */
    private <T> boolean isPrimitive(@NotNull String path, @NotNull Function<JsonPrimitive, T> mapper) {
        JsonElement element = getElement(path);
        try {
            return JsonUtils.convertToJsonPrimitive(element).map(mapper).isPresent();
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Get a primitive or the default value
     *
     * @param path   Path to the object
     * @param mapper Mapper function
     * @param def    Default value
     * @param <T>    Type of the result
     * @return Result
     */
    private <T> T getPrimitive(@NotNull String path, @NotNull Function<JsonPrimitive, T> mapper, T def) {
        JsonElement element = getElement(path);
        try {
            return JsonUtils.convertToJsonPrimitive(element).map(mapper).orElse(def);
        } catch (Exception ignored) {
            return def;
        }
    }

    /**
     * Map a list of primitives to a given type.
     * Maps as many as possible values
     *
     * @param path   Path to the object
     * @param mapper Mapper function
     * @param <T>    Result type
     * @return Result
     */
    private <T> List<T> mapPrimitiveList(@NotNull String path, Function<JsonPrimitive, T> mapper) {
        JsonElement element = getElement(path);
        if (!element.isJsonArray()) return Collections.emptyList();
        JsonArray array = element.getAsJsonArray();
        List<T> result = new LinkedList<>();
        for (JsonElement e : array) {
            try {
                JsonUtils.convertToJsonPrimitive(e).map(mapper).ifPresent(result::add);
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    /**
     * Get an element by path
     *
     * @param path Path to the object
     * @return element
     */
    private JsonElement getElement(@NotNull String path) {
        if (path.length() == 0) return config;

        Queue<String> paths = new LinkedList<>(Arrays.asList(path.split("\\.")));
        JsonElement result = config;

        while (!paths.isEmpty()) {
            if (!(result instanceof JsonObject object) || !object.has(paths.peek())) return JsonNull.INSTANCE;
            result = object.get(paths.remove());
        }

        return result;
    }

    /**
     * Create a new section
     *
     * @param path Path to the section
     * @return new section
     */
    private JsonObject createSection(@NotNull String path) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(path), "Cannot create path at parent root!");

        Queue<String> paths = new LinkedList<>(Arrays.asList(path.split("\\.")));
        JsonObject childConfig = config;

        while (!paths.isEmpty()) {
            String nextPath = paths.remove();
            if (childConfig.get(nextPath) instanceof JsonObject next) childConfig = next;
            else {
                JsonObject next = new JsonObject();
                childConfig.add(nextPath, next);
                childConfig = next;
            }
        }
        return childConfig;
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
        JsonElement element = getElement(path);
        return JsonUtils.unpackJson(element);
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
        return isPrimitive(path, JsonPrimitive::getAsBoolean);
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
        return getPrimitive(path, JsonPrimitive::getAsBoolean, def);
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
        return mapPrimitiveList(path, JsonPrimitive::getAsBoolean);
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
        return isPrimitive(path, JsonPrimitive::getAsByte);
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
        return getPrimitive(path, JsonPrimitive::getAsByte, def);
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
        return mapPrimitiveList(path, JsonPrimitive::getAsByte);
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
        return isPrimitive(path, JsonPrimitive::getAsShort);
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
        return getPrimitive(path, JsonPrimitive::getAsShort, def);
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
        return mapPrimitiveList(path, JsonPrimitive::getAsShort);
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
        return isPrimitive(path, JsonPrimitive::getAsInt);
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
        return getPrimitive(path, JsonPrimitive::getAsInt, def);
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
        return mapPrimitiveList(path, JsonPrimitive::getAsInt);
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
        return isPrimitive(path, JsonPrimitive::getAsLong);
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
        return getPrimitive(path, JsonPrimitive::getAsLong, def);
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
        return mapPrimitiveList(path, JsonPrimitive::getAsLong);
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
        return isPrimitive(path, JsonPrimitive::getAsFloat);
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
        return getPrimitive(path, JsonPrimitive::getAsFloat, def);
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
        return mapPrimitiveList(path, JsonPrimitive::getAsFloat);
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
        return isPrimitive(path, JsonPrimitive::getAsDouble);
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
        return getPrimitive(path, JsonPrimitive::getAsDouble, def);
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
        return mapPrimitiveList(path, JsonPrimitive::getAsDouble);
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
        return mapPrimitiveList(path, JsonPrimitive::getAsCharacter);
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
        return isPrimitive(path, JsonPrimitive::isString);
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
        JsonElement element = getElement(path);
        return JsonUtils.convertJsonElementToString(element);
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
        return mapPrimitiveList(path, JsonPrimitive::getAsString);
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
        return new JsonConfig(getRoot(), this, createSection(path));
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
        return !getElement(path).isJsonNull();
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
        return config.keySet();
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
        return JsonUtils.convertJsonObjectToMap(config);
    }

    /**
     * Saves this {@link Config} to a string, and returns it.
     *
     * @return String containing this config.
     */
    @Override
    public @NotNull String saveToString() {
        return config.toString();
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
        Preconditions.checkArgument(!Strings.isNullOrEmpty(path), "Cannot set to an empty path!");
        Preconditions.checkArgument(!path.startsWith("."), "Invalid path");

        JsonElement element = JsonUtils.packJson(GSON, value);
        int beginIndexOfProperty = path.lastIndexOf('.') + 1;
        String property = path.substring(beginIndexOfProperty);

        JsonObject jsonPath;
        if (beginIndexOfProperty == 0) jsonPath = config;
        else jsonPath = createSection(path.substring(0, beginIndexOfProperty));
        jsonPath.add(property, element);
    }
}

