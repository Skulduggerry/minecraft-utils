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

package me.skulduggerry.utilities.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * Helper class to work with files.
 *
 * @author Skulduggerry
 * @since 0.0.1
 */
public final class FileUtils {

    /**
     * Constructor.
     */
    private FileUtils() {
    }

    /**
     * Creates the file at the given path and all directories.
     *
     * @param path the path.
     * @throws IOException If an I/O error occurs.
     */
    public static void createFileIfNotExists(@NotNull Path path) throws IOException {
        if (Files.exists(path))
            return;

        if (Files.isDirectory(path))
            throw new IllegalArgumentException("Given past must be a file!");

        Files.createDirectories(path.getParent());
        Files.createFile(path);
    }

    /**
     * Opens a file for reading, returning a {@code BufferedReader} to read text
     * from the file in an efficient manner. Bytes from the file are decoded into
     * characters using the {@link StandardCharsets#UTF_8 UTF-8} {@link Charset
     * charset}.
     *
     * <p> This method works as if invoking it were equivalent to evaluating the
     * expression:
     * <blockquote>{@link #newBufferedReader(Path, Charset)
     * newBufferedReader(path, StandardCharsets.UTF_8)
     * }</blockquote>
     *
     * @param path the path to the file
     * @return a new buffered reader, with default buffer size, to read text
     * from the file
     * @throws IOException if an I/O error occurs opening the file
     */
    public static Reader newBufferedReader(@NotNull Path path) throws IOException {
        return Files.newBufferedReader(path);
    }

    /**
     * Opens a file for reading, returning a {@code BufferedReader} that may be
     * used to read text from the file in an efficient manner. Bytes from the
     * file are decoded into characters using the specified charset. Reading
     * commences at the beginning of the file.
     *
     * <p> The {@code Reader} methods that read from the file throw {@code
     * IOException} if a malformed or unmappable byte sequence is read.
     *
     * @param path    the path to the file
     * @param charset the charset to use for decoding
     * @return a new buffered reader, with default buffer size, to read text
     * from the file
     * @throws IOException if an I/O error occurs opening the file
     */
    public static Reader newBufferedReader(@NotNull Path path, @NotNull Charset charset) throws IOException {
        return Files.newBufferedReader(path, charset);
    }

    /**
     * Opens a file for reading, returning a {@code BufferedReader} that may be
     * used to read text from the file in an efficient manner. Bytes from the
     * file are decoded into characters using the specified charset. Reading
     * commences at the beginning of the file.
     *
     * <p> The {@code Reader} methods that read from the file throw {@code
     * IOException} if a malformed or unmappable byte sequence is read.
     *
     * @param path the path to the file
     * @return a new buffered reader, with default buffer size, to read text
     * from the file
     * @throws IOException if an I/O error occurs opening the file
     * @see Files#readAllLines
     */
    public static BufferedWriter newBufferedWriter(@NotNull Path path, @NotNull OpenOption... options) throws IOException {
        return Files.newBufferedWriter(path, options);
    }

    /**
     * Opens or creates a file for writing, returning a {@code BufferedWriter}
     * that may be used to write text to the file in an efficient manner.
     * The {@code options} parameter specifies how the file is created or
     * opened. If no options are present then this method works as if the {@link
     * StandardOpenOption#CREATE CREATE}, {@link
     * StandardOpenOption#TRUNCATE_EXISTING TRUNCATE_EXISTING}, and {@link
     * StandardOpenOption#WRITE WRITE} options are present. In other words, it
     * opens the file for writing, creating the file if it doesn't exist, or
     * initially truncating an existing {@link Files#isRegularFile regular-file} to
     * a size of {@code 0} if it exists.
     *
     * <p> The {@code Writer} methods to write text throw {@code IOException}
     * if the text cannot be encoded using the specified charset.
     *
     * @param path    the path to the file
     * @param charset the charset to use for encoding
     * @param options options specifying how the file is opened
     * @return a new buffered writer, with default buffer size, to write text
     * to the file
     * @throws IllegalArgumentException      if {@code options} contains an invalid combination of options
     * @throws IOException                   if an I/O error occurs opening or creating the file
     * @throws UnsupportedOperationException if an unsupported option is specified
     * @throws FileAlreadyExistsException    If a file of that name already exists and the {@link
     *                                       StandardOpenOption#CREATE_NEW CREATE_NEW} option is specified
     *                                       <i>(optional specific exception)</i>
     * @see Files#write(Path, Iterable, Charset, OpenOption[])
     */
    public static BufferedWriter newBufferedWriter(@NotNull Path path,@NotNull Charset charset, @NotNull OpenOption... options) throws IOException {
        return Files.newBufferedWriter(path, charset, options);
    }

    /**
     * Gets the extension of a fileName.
     * <p>
     * This method returns the textual part of the fileName after the last dot.
     * There must be no directory separator after the dot.
     * <pre>
     * foo.txt      --&gt; "txt"
     * a/b/c.jpg    --&gt; "jpg"
     * a/b.txt/c    --&gt; ""
     * a/b/c        --&gt; ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on, with the
     * exception of a possible {@link IllegalArgumentException} on Windows (see below).
     * </p>
     * <p>
     * <b>Note:</b> This method used to have a hidden problem for names like "foo.exe:bar.txt".
     * In this case, the name wouldn't be the name of a file, but the identifier of an
     * alternate data stream (bar.txt) on the file foo.exe. The method used to return
     * ".txt" here, which would be misleading. Commons IO 2.7, and later versions, are throwing
     * an {@link IllegalArgumentException} for names like this.
     *
     * @param fileName the fileName to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists or {@code null}
     * if the fileName is {@code null}.
     * @throws IllegalArgumentException <b>Windows only:</b> The fileName parameter is, in fact,
     *                                  the identifier of an Alternate Data Stream, for example "foo.exe:bar.txt".
     */
    @Contract("!null -> !null; null -> null")
    public static String getExtension(String fileName) {
        return org.apache.commons.io.FilenameUtils.getExtension(fileName);
    }
}
