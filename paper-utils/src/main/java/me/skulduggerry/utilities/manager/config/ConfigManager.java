package me.skulduggerry.utilities.manager.config;

import me.skulduggerry.utilities.config.Config;
import me.skulduggerry.utilities.config.wrapper.FileConfig;
import me.skulduggerry.utilities.manager.Manager;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface which represents a manager for {@link Config}s.
 * It allows to load {@link Config}s from files.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public interface ConfigManager extends Manager {

    /**
     * Get the config from a file.
     *
     * @param path The path to this file.
     * @return The loaded config for this path.
     * @throws IOException If an I/O error occurs.
     */
    @NotNull
    FileConfig getConfigFromFile(@NotNull String path) throws IOException;

    /**
     * Get the config from a file.
     *
     * @param path The path to this file.
     * @return The loaded config for this path.
     * @throws IOException If an I/O error occurs.
     */
    @NotNull
    FileConfig getConfigFromFile(@NotNull Path path) throws IOException;

    /**
     * Add support for new  Config types.
     *
     * @param clazz      Class of the config
     * @param extension  Min required extension
     * @param extensions More extensions
     */
    void addSupport(@NotNull Class<? extends Config> clazz, @Pattern("[a-z]+") String extension, String... extensions);

    /**
     * Saves all configs loaded by this class to their file.
     *
     * @throws IOException Tries to save all configs.
     *                     When an exception occurs it will be rethrown after all configs where saved.
     *                     Throws always the last exception that occurs.
     */
    void saveAll() throws IOException;
}
