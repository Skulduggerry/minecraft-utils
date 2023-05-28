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

