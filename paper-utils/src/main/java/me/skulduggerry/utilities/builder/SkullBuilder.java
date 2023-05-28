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

package me.skulduggerry.utilities.builder;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.skulduggerry.utilities.utils.ReflectionUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

/**
 * Class which allows to build {@link Material#PLAYER_HEAD}s with custom textures.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class SkullBuilder extends ItemBuilder<SkullBuilder> {

    /**
     * An skull builder with no extra data.
     *
     * @param amount stack size
     */
    public SkullBuilder(@Range(from = 1, to = 64) int amount) {
        super(Material.PLAYER_HEAD, amount);
    }

    /**
     * An skull builder with no extra data.
     */
    public SkullBuilder() {
        this(1);
    }

    /**
     * Sets the owner of the skull.
     * <p>
     * Plugins should check that hasOwner() returns true before calling this
     * plugin.
     *
     * @param player the new owner of the skull
     * @return the builder
     */
    public SkullBuilder setOwningPlayer(@NotNull OfflinePlayer player) {
        ((SkullMeta) meta).setOwningPlayer(player);
        return this;
    }

    /**
     * Sets the profile of the player who owns the skull. This player profile
     * may appear as the texture depending on skull type.
     * <p>
     * The profile must contain both a unique id and a skin texture. If either
     * of these is missing, the profile must contain a name by which the server
     * will then attempt to look up the unique id and skin texture.
     *
     * @param profile the profile of the owning player
     * @throws IllegalArgumentException if the profile does not contain the
     *                                  necessary information
     */
    public SkullBuilder setOwnerProfile(@NotNull PlayerProfile profile) {
        ((SkullMeta) meta).setPlayerProfile(profile);
        return this;
    }

    /**
     * Set the texture of the head.
     *
     * @param value The value for the texture.
     * @return The builder.
     */
    public SkullBuilder setTexture(@NotNull String value) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value));
        Optional<Method> optionalMethod = ReflectionUtils.getMethod("setProfile", GameProfile.class);

        if (optionalMethod.isEmpty())
            throw new RuntimeException("Missing method setProfile in GameProfile.class");

        try {
            Method method = optionalMethod.get();
            method.setAccessible(true);
            method.invoke(meta, profile);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Error while setting texture", e);
        }
        return this;
    }
}

