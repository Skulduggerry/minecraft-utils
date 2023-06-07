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
    @NotNull
    static TypeAdapterFactory getFactory(@NotNull Class<?> clazz, @NotNull JsonAdapter<?> adapter) {
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
     * @throws IOException if an I/O error occurs
     */
    void write(@NotNull Gson gson, @NotNull JsonWriter writer, @Nullable T value) throws IOException;

    /**
     * Reade's an object from a json config
     *
     * @param gson   The gson to convert into not out-of-the-box supported class
     * @param reader The input from the config
     * @return The object
     * @throws IOException if an I/O error occurs
     */
    @Nullable
    T read(@NotNull Gson gson, @NotNull JsonReader reader) throws IOException;

    /**
     * Converts the adapter to a {@see TypeAdapter}
     *
     * @param gson The gson to convert into not out-of-the-box supported class
     * @return The adapter
     */
    @NotNull
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
