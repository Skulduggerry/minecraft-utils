package me.skulduggerry.utilities.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.skulduggerry.utilities.utils.JsonUtils;
import me.skulduggerry.utilities.utils.SerializationUtils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;

/**
 * @author Skulduggerry
 * @since 0.1.0
 */
public class ConfigurationSerializableTypeAdapter implements JsonAdapter<ConfigurationSerializable> {

    /**
     * Writes a {@see ConfigurationSerializable} to a json config
     *
     * @param gson   The gson to convert into not out-of-the-box supported class
     * @param writer Output to the config
     * @param value  Value to write
     */
    @Override
    public void write(@NotNull Gson gson, @NotNull JsonWriter writer, @Nullable ConfigurationSerializable value) throws IOException {
        JsonObject result = new JsonObject();
        result.addProperty(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(value.getClass()));
        JsonUtils.addAllProperties(gson, value.serialize(), result);
        TypeAdapters.JSON_ELEMENT.write(writer, result);
    }

    /**
     * Reade's a {@see ConfigurationSerializable} from a json config
     *
     * @param gson   The gson to convert into not out-of-the-box supported class
     * @param reader The input from the config
     * @return The serialized object
     */
    @Override
    public ConfigurationSerializable read(@NotNull Gson gson, @NotNull JsonReader reader) {
        JsonObject object = gson.fromJson(reader, JsonObject.class);
        Map<String, Object> map = JsonUtils.convertJsonObjectToMap(object);
        return SerializationUtils.deserialize(map);
    }
}
