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

package me.skulduggerry.utilities.manager.config.type;

import me.skulduggerry.utilities.config.Config;
import me.skulduggerry.utilities.config.type.JsonConfig;
import me.skulduggerry.utilities.config.type.YamlConfig;
import me.skulduggerry.utilities.config.wrapper.FileConfig;
import me.skulduggerry.utilities.exception.UnsupportedFileExtension;
import me.skulduggerry.utilities.manager.config.ConfigManager;
import me.skulduggerry.utilities.utils.FileUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the {@link ConfigManager} interface.
 * It allows to load {@link Config}s from files.
 *
 * @author Skulduggerry
 * @since 1.2.0
 */
public class SimpleConfigManager implements ConfigManager {

    private final Map<String, FileConfig> configs;

    private final Map<String, Class<? extends Config>> supported;

    /**
     * Constructor.
     */
    public SimpleConfigManager() {
        this.configs = new HashMap<>();
        supported = new HashMap<>();
    }

    /**
     * Get the config from a file.
     *
     * @param path The path to this file.
     * @return The loaded config for this path.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public @NotNull FileConfig getConfigFromFile(@NonNull String path) throws IOException {
        return getConfigFromFile(Paths.get(path));
    }

    /**
     * Get the config from a file.
     *
     * @param path The path to this file.
     * @return The loaded config for this path.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public @NotNull FileConfig getConfigFromFile(@NonNull Path path) throws IOException {
        if (Files.isDirectory(path))
            throw new IllegalArgumentException("Given path must not be a directory!");

        Class<? extends Config> type = getClassForFileExtension(path.toString());

        FileUtils.createFileIfNotExists(path);
        path = path.toRealPath();

        if (configs.containsKey(path.toString()))
            return configs.get(path.toString());

        FileConfig config = FileConfig.loadFromFile(path, type);
        configs.put(path.toString(), config);
        return config;
    }

    /**
     * Get the class of config for the given extension.
     *
     * @param filename The name of the config.
     * @return The class for the extension.
     */
    private Class<? extends Config> getClassForFileExtension(@NonNull String filename) {
        String fileExtension = FileUtils.getExtension(filename);

        if (supported.containsKey(fileExtension)) return supported.get(fileExtension);

        return switch (fileExtension) {
            case "json" -> JsonConfig.class;
            case "yml", "yaml" -> YamlConfig.class;
            default -> throw new UnsupportedFileExtension(fileExtension);
        };
    }

    @Override
    public void addSupport(@NonNull Class<? extends Config> clazz, @NonNull String extension, String... extensions) {
        supported.put(extension, clazz);
        Arrays.stream(extensions).forEach(e -> supported.put(e, clazz));
    }

    /**
     * Saves all configs to their files.
     */
    @Override
    public void saveAll() throws IOException {
        IOException exception = null;

        for (FileConfig config : configs.values()) {
            try {
                config.saveToFile();
            } catch (IOException e) {
                exception = e;
            }

        }

        if (exception != null) throw exception;
    }

    /**
     * Handle enable of the manager.
     */
    @Override
    public void handleEnable() {
    }

    /**
     * Handle disable of the manager.
     */
    @Override
    public void handleDisable() {
        try {
            saveAll();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
