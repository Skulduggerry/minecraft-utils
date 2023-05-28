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
