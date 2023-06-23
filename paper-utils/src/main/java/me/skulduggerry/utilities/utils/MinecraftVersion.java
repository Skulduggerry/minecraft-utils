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
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Helper cass for minecraft versions
 */
public class MinecraftVersion {

    /**
     * Minecraft 1.7
     */
    public static final Version V_7 = new Version(1, 7, 0);

    /**
     * Minecraft 1.8
     */
    public static final Version V_8 = new Version(1, 8, 0);

    /**
     * Minecraft 1.9
     */
    public static final Version V_9 = new Version(1, 9, 0);

    /**
     * Minecraft 1.10
     */
    public static final Version V_10 = new Version(1, 10, 0);

    /**
     * Minecraft 1.11
     */
    public static final Version V_11 = new Version(1, 11, 0);

    /**
     * Minecraft 1.12
     */
    public static final Version V_12 = new Version(1, 12, 0);

    /**
     * Minecraft 1.13
     */
    public static final Version V_13 = new Version(1, 13, 0);

    /**
     * Minecraft 1.14
     */
    public static final Version V_14 = new Version(1, 14, 0);

    /**
     * Minecraft 1.15
     */
    public static final Version V_15 = new Version(1, 15, 0);

    /**
     * Minecraft 1.16
     */
    public static final Version V_16 = new Version(1, 16, 0);

    /**
     * Minecraft 1.17
     */
    public static final Version V_17 = new Version(1, 17, 0);

    /**
     * Minecraft 1.18
     */
    public static final Version V_18 = new Version(1, 18, 0);

    /**
     * Minecraft 1.19
     */
    public static final Version V_19 = new Version(1, 19, 0);

    /**
     * Minecraft 1.20
     */
    public static final Version V_20 = new Version(1, 20, 0);

    /**
     * Compares two versions exact
     */
    public static final Comparator<Version> COMPARATOR_EXACT = Version::compareTo;

    /**
     * Compares only major and minor part of the version
     */
    public static final Comparator<Version> COMPARATOR = Comparator.comparing(Version::getMajor).thenComparing(Version::getMajor);

    /**
     * Get the version of the server without revision.
     *
     * @return The version
     */
    @NotNull
    public static Version getServerVersion() {
        Version exactVersion = getServerVersionExact();
        return new Version(exactVersion.getMajor(), exactVersion.getMinor(), 0);
    }

    /**
     * Gte the full version of the server
     *
     * @return The version.
     */
    @NotNull
    public static Version getServerVersionExact() {
        String versionString = Bukkit.getBukkitVersion().split("-")[0];
        return VersionUtils.parseExceptionally(versionString);
    }

    /**
     * Constructor.
     */
    private MinecraftVersion() {
    }
}
