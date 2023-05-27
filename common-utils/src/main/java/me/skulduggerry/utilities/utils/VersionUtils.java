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
