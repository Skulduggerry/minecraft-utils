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

package me.skulduggerry.utilities;

import me.skulduggerry.utilities.config.Config;
import me.skulduggerry.utilities.config.type.YamlConfig;
import me.skulduggerry.utilities.manager.config.ConfigManager;
import me.skulduggerry.utilities.manager.config.type.SimpleConfigManager;
import me.skulduggerry.utilities.requirements.RequirementsChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class ExtendedPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private Config pluginYaml;
    private Config configYaml;
    private Map<Class<? extends Listener>, Listener> listeners;

    /**
     * Called when the plugin first loads
     */
    @Override
    public final void onLoad() {
        super.onLoad();
        createManagers();
        listeners = new HashMap<>();
    }

    /**
     * Create the managers.
     */
    protected void createManagers() {
        this.configManager = new SimpleConfigManager();
    }

    /**
     * Called after a plugin is loaded but before it has been enabled.
     * <p>
     * When multiple plugins are loaded, the onLoad() for all plugins is
     * called before any onEnable() is called.
     */
    @Override
    public final void onEnable() {
        super.onEnable();
    }

    /**
     * Enable the managers.
     */
    protected void enableManagers() {
        configManager.handleEnable();
    }

    /**
     * Check the requirements of this plugin.
     */
    private void checkRequirements() {
        RequirementsChecker requirementsChecker = new RequirementsChecker(this, getConfigYaml());
        requirementsChecker.checkRequirements();
    }

    /**
     * Registers all {@link CommandExecutor}s.
     */
    protected void registerCommands() {
    }

    /**
     * Registers all {@link Listener}s.
     */
    protected void registerListeners() {
    }

    /**
     * Called when this plugin is disabled
     */
    @Override
    public final void onDisable() {
        super.onDisable();
        disableManagers();
    }

    /**
     * Disable the managers.
     */
    protected void disableManagers() {
        configManager.handleDisable();
    }

    /**
     * Get hte plugin.yml file.
     *
     * @return The file as a config.
     */
    public Config getPluginYaml() {
        if (pluginYaml != null) return pluginYaml;
        return pluginYaml = new YamlConfig(new InputStreamReader(
                Objects.requireNonNull(getResource("plugin.yml")),
                StandardCharsets.UTF_8
        )).readOnly();
    }

    /**
     * Get the config.yml file.
     *
     * @return The file as config.
     */
    public Config getConfigYaml() {
        if (configYaml != null) return configYaml;
        saveDefaultConfig();
        Path configPath = getDataFolder().toPath().resolve("config.yml");
        return configYaml = getConfig(configPath);
    }

    /**
     * Get the {@link Config} at the given {@link Path}.
     * If the config does not exist, a new file will be created and loaded as config.
     * If a IO-exception occurs a {@link UncheckedIOException} will be thrown.
     *
     * @param file The path to the config.
     * @return The {@link Config}.
     */
    public Config getConfig(Path file) {
        try {
            return configManager.getConfigFromFile(file);
        } catch (IOException e) {
            getLogger().severe(() -> "Cannot load config at path: %s".formatted(file));
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Register a {@link CommandExecutor}.
     *
     * @param name     The name of the command.
     * @param executor The {@link CommandExecutor} for the command.
     */
    public void registerCommand(@NonNull String name, @NonNull CommandExecutor executor) {
        PluginCommand command = Objects.requireNonNull(getCommand(name));
        command.setExecutor(executor);
    }

    /**
     * Register a {@link TabCompleter}.
     *
     * @param name      The name of the command.
     * @param completer The {@link TabCompleter} for the command.
     */
    public void registerTabComplete(@NonNull String name, @NonNull TabCompleter completer) {
        PluginCommand command = Objects.requireNonNull(getCommand(name));
        command.setTabCompleter(completer);
    }

    /**
     * Register a class which implements {@link CommandExecutor} and {@link TabCompleter}.
     *
     * @param name The name of the command.
     * @param t    The which executes the command and provides the {@link TabCompleter}.
     * @param <T>  The type of the class.
     */
    public <T extends CommandExecutor & TabCompleter> void registerCommandAndTabCompleter(@NonNull String name, @NonNull T t) {
        registerCommand(name, t);
        registerTabComplete(name, t);
    }

    /**
     * Register a {@link Listener}.
     *
     * @param listener The listener.
     */
    public void registerListener(@NonNull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
        listeners.put(listener.getClass(), listener);
    }

    /**
     * Unregister a {@link Listener}.
     *
     * @param listener The {@link Class} of the {@link Listener}.
     */
    public void unregisterListener(@NonNull Class<? extends Listener> listener) {
        HandlerList.unregisterAll(listeners.remove(listener));
    }
}
