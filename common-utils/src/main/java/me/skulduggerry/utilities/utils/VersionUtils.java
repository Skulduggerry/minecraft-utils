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

import me.skulduggerry.utilities.version.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Helper class for versions
 *
 * @author Skulduggerry
 * @since 0.0.1
 */
public class VersionUtils {

    /**
     * Constructor
     */
    private VersionUtils() {
    }

    /**
     * Parses the given string to a version.
     * If the string does not represent a valid version, this method will return null.
     *
     * @param version The version as string.
     * @return The version.
     */
    @Nullable
    public static Version parse(@Nullable String version) {
        return parse(version, null);
    }

    /**
     * Parses the given string to a version.
     * If the string does not represent a valid version, this method will return the default value.
     *
     * @param version The version string.
     * @param def     The default value.
     * @return The version.
     */
    @Nullable
    public static Version parse(@Nullable String version, @Nullable Version def) {
        try {
            if (version == null) return def;
            return parseExceptionally(version);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Tries to parse the given string to a version.
     * If the string does not represent a valid version, this method will throw an {@link IllegalArgumentException}.
     *
     * @param version The version string.
     * @return The version.
     */
    @NotNull
    public static Version parseExceptionally(@NotNull String version) {
        String[] array = version.split("\\.");
        if (array.length == 0)
            throw new IllegalArgumentException("Version must not be empty");
        try {
            int major = Integer.parseInt(array[0]);
            int minor = array.length == 2 ? Integer.parseInt(array[1]) : 0;
            int revision = array.length == 3 ? Integer.parseInt(array[2]) : 0;

            if (major < 0 || minor < 0 || revision < 0)
                throw new IllegalArgumentException("Numbers in version must not be null!");

            return new Version(major, minor, revision);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse version: " + version + " because '" + e.getMessage() + "'", e);
        }
    }
}
