package me.skulduggerry.utilities.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Interface for json adapters
 *
 * @param <T> Type to convert to
 * @author Skulduggerry
 * @since 0.0.1
 */
public interface JsonAdapter<T> {

    /**
     * Converts the adapter to a factory
     *
     * @param clazz   Class to serialize
     * @param adapter The adapter
     * @return The factory.
     */
    @SuppressWarnings("unchecked")
    static TypeAdapterFactory getFactory(Class<?> clazz, JsonAdapter<?> adapter) {
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                return clazz.isAssignableFrom(type.getRawType()) ? (TypeAdapter<T>) adapter.getAsTypeAdapter(gson) : null;
            }
        };
    }

    /**
     * Writes an object to a json config
     *
     * @param gson   The gson to convert into not out-of-the-box supported class
     * @param writer Output to the config
     * @param value  Value to write
     */
    void write(@NotNull Gson gson, @NotNull JsonWriter writer, @Nullable T value) throws IOException;

    /**
     * Reade's an object from a json config
     *
     * @param gson   The gson to convert into not out-of-the-box supported class
     * @param reader The input from the config
     * @return The object
     */
    @Nullable
    T read(@NotNull Gson gson, @NotNull JsonReader reader);

    /**
     * Converts the adapter to a {@see TypeAdapter}
     *
     * @param gson The gson to convert into not out-of-the-box supported class
     * @return The adapter
     */
    default TypeAdapter<T> getAsTypeAdapter(@NotNull Gson gson) {
        return new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                JsonAdapter.this.write(gson, out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                return JsonAdapter.this.read(gson, in);
            }
        };
    }
}
