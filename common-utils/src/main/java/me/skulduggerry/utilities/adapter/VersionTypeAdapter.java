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
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.skulduggerry.utilities.utils.VersionUtils;
import me.skulduggerry.utilities.version.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Type adapter for versions
 *
 * @author Skulduggerry
 * @since 0.0.1
 */
public class VersionTypeAdapter implements JsonAdapter<Version> {

    /**
     * Writes an object to a json config
     *
     * @param gson   The gson to convert into not out-of-the-box supported class
     * @param writer Output to the config
     * @param value  Value to write
     */
    @Override
    public void write(@NotNull Gson gson, @NotNull JsonWriter writer, @Nullable Version value) throws IOException {
        TypeAdapters.STRING.write(writer, value == null ? null : value.toString());
    }

    /**
     * Reade's an object from a json config
     *
     * @param gson   The gson to convert into not out-of-the-box supported class
     * @param reader The input from the config
     * @return The object
     */
    @Override
    @Nullable
    public Version read(@NotNull Gson gson, @NotNull JsonReader reader) {
        try {
            String version = TypeAdapters.STRING.read(reader);
            return VersionUtils.parse(version);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
