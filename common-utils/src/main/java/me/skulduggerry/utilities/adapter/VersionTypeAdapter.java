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
