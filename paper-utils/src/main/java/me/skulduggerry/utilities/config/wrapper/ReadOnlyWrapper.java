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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Wrapper for read only configs.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class ReadOnlyWrapper extends ConfigWrapper {

    private ReadOnlyWrapper(@NotNull Config wrapped) {
        super(wrapped);
    }

    @NotNull
    public static Config asReadOnlyConfig(@NotNull Config config) {
        return config.isReadOnly() ? config : new ReadOnlyWrapper(config);
    }

    /**
     * Gets the root {@link Config} that contains this {@link Config}
     * <p>
     * If this {@link Config} is the root it will return itself.
     * The returned config is read only!
     *
     * @return Root {@link Config} containing this {@link Config}.
     */
    @Override
    public @NotNull Config getRoot() {
        Config root = getWrappedConfig().getRoot();
        return asReadOnlyConfig(root);
    }

    /**
     * Gets the parent {@link Config} that directly contains
     * this {@link Config}.
     * <p>
     * If this {@link Config} is the root it will return null.
     * If parent is not null the returned config is read only!
     *
     * @return Parent {@link Config} containing this {@link Config}.
     */
    @Override
    public @NotNull Optional<Config> getParent() {
        Optional<Config> parent = getWrappedConfig().getParent();
        if (parent.isPresent()) {
            parent = Optional.of(asReadOnlyConfig(parent.get()));
        }
        return parent;
    }

    /**
     * Gets the requested child config by path.
     * <p>
     * If the child config does not exist this will create a new config.
     * The returned config is read only!
     *
     * @param path Path of the child config to get.
     * @return Requested child config.
     */
    @Override
    public @NotNull Config getConfig(@NotNull String path) {
        Config config = getWrappedConfig().getConfig(path);
        return asReadOnlyConfig(config);
    }

    /**
     * @param path  ---
     * @param value ---
     * @throws UnsupportedOperationException This config is read only and cannot be modified.
     */
    @Override
    public void set(@NotNull String path, @Nullable Object value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("set(String, Object) is not supported in a read-only config!");
    }

    /**
     * @param path ---
     * @throws UnsupportedOperationException This config is read only and cannot be modified.
     */
    @Override
    public void remove(@NotNull String path) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("remove(String) is not supported in a read only config!");
    }

    /**
     * @throws UnsupportedOperationException This config is read only and cannot be modified.
     */
    @Override
    public void clear() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("clear() is not supported in a read only config!");
    }

    /**
     * @param config               ---
     * @param overrideExistingKeys ---
     * @throws UnsupportedOperationException This config is read only and cannot be modified.
     */
    @Override
    public void addAll(@NotNull Config config, boolean overrideExistingKeys) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("addAll(Config, boolean) is not supported in a read only config!");
    }
}
