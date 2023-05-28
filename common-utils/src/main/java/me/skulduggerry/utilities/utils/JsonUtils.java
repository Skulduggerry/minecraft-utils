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

package me.skulduggerry.utilities.utils;

import com.google.gson.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Helper class to work with json.
 *
 * @author Skulduggerry
 * @since 0.0.1
 */
public class JsonUtils {

    /**
     * Constructor
     */
    private JsonUtils() {
    }

    /**
     * Tries to convert a json element to an object.
     *
     * @param element The element
     * @return The unpacked object
     */
    @Nullable
    public static Object unpackJson(@Nullable JsonElement element) {
        if (element == null || element.isJsonNull()) return null;
        if (element.isJsonArray()) return convertJsonArrayToStringList(element.getAsJsonArray());
        if (element.isJsonObject()) return convertJsonObjectToMap(element.getAsJsonObject());

        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isBoolean()) return primitive.getAsBoolean();
            if (primitive.isNumber()) return primitive.getAsNumber();
            if (primitive.isString()) return primitive.getAsString();
        }
        return element;
    }

    /**
     * Tries to convert a json element to of the given class
     *
     * @param gson    The gson to convert into not out-of-the-box supported class
     * @param element The element
     * @param type    The type of the result object
     * @return The unpacked object.
     */
    @Nullable
    public static Object unpackJson(@NotNull Gson gson, @Nullable JsonElement element, @NotNull Class<?> type) {
        return gson.fromJson(element, type);
    }

    /**
     * Tries to an object to a json element.
     *
     * @param gson   The gson to convert into not out-of-the-box supported class
     * @param object Object to convert.
     * @return The json element.
     */
    @NotNull
    public static JsonElement packJson(@NotNull Gson gson, @Nullable Object object) {
        if (object == null) return JsonNull.INSTANCE;
        if (object instanceof JsonElement jsonElement) return jsonElement;
        if (object instanceof Number number) return new JsonPrimitive(number);
        if (object instanceof String string) return new JsonPrimitive(string);
        if (object instanceof Boolean bool) return new JsonPrimitive(bool);
        if (object instanceof Character character) return new JsonPrimitive(character);
        if (object instanceof Object[] array) return convertArrayToJsonArray(gson, array);
        if (object instanceof Iterable<?> iterable) return convertIterableToJsonArray(gson, iterable);
        if (object instanceof Map<?, ?> map) return convertMapToJsonObject(gson, map);
        if (object instanceof Iterator<?> iterator) return convertIteratorToJsonArray(gson, iterator);
        return gson.toJsonTree(object);
    }

    /**
     * Makes a json element to a string
     *
     * @param element The element
     * @return The string
     */
    @Nullable
    @Contract("null -> null")
    public static String convertJsonElementToString(@Nullable JsonElement element) {
        if (element == null || element.isJsonNull()) return null;
        if (element.isJsonPrimitive()) {
            return element.getAsJsonPrimitive().getAsString();
        }
        return element.toString();
    }

    /**
     * Makes a json array into a string array
     *
     * @param element The json array
     * @return The array
     */
    @NotNull
    public static List<String> convertJsonArrayToStringList(@NotNull JsonArray element) {
        List<String> list = new ArrayList<>(element.size());
        element.forEach(e -> list.add(convertJsonElementToString(e)));
        return list;
    }

    /**
     * Makes an array to a json object
     *
     * @param gson  The gson to convert into not out-of-the-box supported class
     * @param array The array
     * @return The json array
     */
    @NotNull
    public static JsonArray convertArrayToJsonArray(@NotNull Gson gson, @NotNull Object[] array) {
        JsonArray result = new JsonArray(array.length);
        for (Object o : array) {
            JsonElement element = packJson(gson, o);
            result.add(element);
        }
        return result;
    }

    /**
     * Converts an iterable to a json array
     *
     * @param gson     The gson to convert into not out-of-the-box supported class
     * @param iterable The iterable
     * @return The json array
     */
    @NotNull
    public static JsonArray convertIterableToJsonArray(@NotNull Gson gson, @NotNull Iterable<?> iterable) {
        return convertIteratorToJsonArray(gson, iterable.iterator());
    }

    /**
     * Converts an iterator into a json array
     *
     * @param gson     The gson to convert into not out-of-the-box supported class
     * @param iterator The iterator
     * @return The json array
     */
    @NotNull
    public static JsonArray convertIteratorToJsonArray(@NotNull Gson gson, @NotNull Iterator<?> iterator) {
        JsonArray result = new JsonArray();
        iterator.forEachRemaining(o -> result.add(packJson(gson, o)));
        return result;
    }

    /**
     * Converts a json object into a map
     *
     * @param object The json object
     * @return The map
     */
    @NotNull
    public static Map<String, Object> convertJsonObjectToMap(@NotNull JsonObject object) {
        Map<String, Object> result = new HashMap<>();
        object.entrySet().forEach(entry -> result.put(entry.getKey(), unpackJson(entry.getValue())));
        return result;
    }

    /**
     * Convert a map into a json object
     *
     * @param gson The gson to convert into not out-of-the-box supported class
     * @param map  The map
     * @return The json object
     */
    @NotNull
    public static JsonObject convertMapToJsonObject(@NotNull Gson gson, @NotNull Map<?, ?> map) {
        JsonObject result = new JsonObject();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            result.add(key, packJson(gson, value));
        }
        return result;
    }

    /**
     * Converts a json element into a primitive value.
     *
     * @param element The element
     * @return The primitive
     */
    @NotNull
    public static Optional<JsonPrimitive> convertToJsonPrimitive(@Nullable JsonElement element) {
        if (element == null || element.isJsonNull() || !element.isJsonPrimitive()) return Optional.empty();
        return Optional.of(element.getAsJsonPrimitive());
    }

    /**
     * Adds all properties of a map to a json object
     *
     * @param gson        The gson to convert into not out-of-the-box supported class
     * @param source      The source of the properties
     * @param destination The destination for the properties.
     */
    public static void addAllProperties(@NotNull Gson gson, @NotNull Map<String, ?> source, @NotNull JsonObject destination) {
        for (Map.Entry<String, ?> entry : source.entrySet()) {
            String key = entry.getKey();
            JsonElement value = packJson(gson, entry.getValue());
            destination.add(key, value);
        }
    }
}
