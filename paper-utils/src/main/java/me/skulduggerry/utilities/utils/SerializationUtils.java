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

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Helper class for serialization and deserialization of objects.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public final class SerializationUtils {

    /**
     * Deserialize an object from the given map.
     *
     * @param serialized The serialized object.
     * @return The deserialized object.
     */
    @Nullable
    public static ConfigurationSerializable deserialize(@NotNull Map<String, Object> serialized) {
        try {
            return ConfigurationSerialization.deserializeObject(serialized);
        } catch (Throwable t) {
            // thrown when map does not contain some important keys
            return null;
        }
    }

    /**
     * Deserialize an object from the given map and with the given class.
     *
     * @param serialized The serialized object.
     * @param clazz      the class of the object.
     * @param <T>        The type of the serialized object.
     * @return The deserialized object.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends ConfigurationSerializable> T deserialize(@NotNull Map<String, Object> serialized, @NotNull Class<T> clazz) {
        try {
            return (T) ConfigurationSerialization.deserializeObject(serialized, clazz);
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Serialize an object to a map.
     * Throws an {@link IllegalArgumentException} of the object does not implement {@link ConfigurationSerializable}.
     *
     * @param object the object.
     * @return The serialized object.
     */
    @NotNull
    public static Map<String, Object> serialize(@NotNull Object object) {
        if (!(object instanceof ConfigurationSerializable serializable))
            throw new IllegalArgumentException("Object is not serializable!");

        return serializable.serialize();
    }
}

