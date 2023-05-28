package me.skulduggerry.utilities.config.wrapper;

import me.skulduggerry.utilities.config.Config;
import me.skulduggerry.utilities.utils.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

/**
 * Class to load/save configs from/to a file.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class FileConfig extends ConfigWrapper {

    private final Path path;

    /**
     * Constructor.
     *
     * @param path The path to the config file.
     * @param type The type of config.
     * @throws IOException If an IOException occurs.
     */
    private FileConfig(@NotNull Path path, @NotNull Class<? extends Config> type) throws IOException {
        super(Config.load(FileUtils.newBufferedReader(path), type));
        this.path = path;
    }

    /**
     * Constructor.
     *
     * @param path The path to the config file.
     * @param type The type of the config.
     * @return The FileConfig object.
     * @throws IOException If an IOException occurs, e.g. the file does not point to a file.
     */
    @NotNull
    public static FileConfig loadFromFile(@NotNull Path path, @NotNull Class<? extends Config> type) throws IOException {
        FileUtils.createFileIfNotExists(path);
        return new FileConfig(path, type);
    }

    /**
     * Calls the wrapped config to write it's content to the file.
     *
     * @throws IOException If an IOException occurs.
     */
    public void saveToFile() throws IOException {
        FileUtils.createFileIfNotExists(path);
        try (Writer writer = FileUtils.newBufferedWriter(path)) {
            save(writer);
        }
    }
}
