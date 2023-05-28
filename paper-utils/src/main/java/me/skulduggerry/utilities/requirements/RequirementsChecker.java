package me.skulduggerry.utilities.requirements;

import me.skulduggerry.utilities.config.Config;
import me.skulduggerry.utilities.utils.MinecraftVersion;
import me.skulduggerry.utilities.version.Version;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * Class to check the requirements this plugin has.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public final class RequirementsChecker {

    private final JavaPlugin plugin;
    private final Logger logger;
    private final Config requirementsConfig;

    /**
     * Constructor.
     *
     * @param plugin             The plugin which uses the RequirementsChecker.
     * @param requirementsConfig The config which
     */
    public RequirementsChecker(@NotNull JavaPlugin plugin, @NotNull Config requirementsConfig) {
        this.plugin = plugin;
        logger = plugin.getLogger();
        this.requirementsConfig = requirementsConfig;
        checkRequirements();
    }

    /**
     * <p>
     * Checks the requirements.
     * </p>
     * <p>
     * The following requirements are checked:
     * <ul>
     *     <li>First it checks whether the plugin requires paper: default true.</li>
     *     <li>Then it checks whether the server requires spigot: default false.</li>
     *     <li>Lastly it checks whether the server requires a specific minimum version to run.</li>
     * </ul>
     * If any of this requirements fails the server will shut down with a message what should be done to fix this.
     * </p>
     */
    public void checkRequirements() {
        if (getRequirement("paper", true)) requiresPaper();
        if (getRequirement("spigot", false)) requiresSpigot();
        if (requirementsConfig.contains("version")) requiresVersion(requirementsConfig.getVersion("version"));
    }

    /**
     * Check weather this server is a spigot server if it is required.
     */
    private void requiresSpigot() {
        try {
            Bukkit.spigot();
        } catch (Exception e) {
            logger.severe(() -> "==============================" + plugin.getName() + "==============================");
            logger.severe(() -> "");
            logger.severe(() -> "This plugin requires Spigot to run.");
            logger.severe(() -> "Please download Spigot or Paper to run this plugin.");
            logger.severe(() -> "");
            logger.severe(() -> "Spigot: https://getbukkit.org/download/spigot");
            logger.severe(() -> "Spigot build-tools: https://www.spigotmc.org/wiki/buildtools");
            logger.severe(() -> "Paper: https://papermc.io/downloads");
            logger.severe(() -> "");
            logger.severe(() -> "==============================" + plugin.getName() + "==============================");
            logger.severe(() -> "");
            Bukkit.shutdown();
        }
    }

    /**
     * Check weather this server is a paper server if it is required.
     */
    private void requiresPaper() {
        try {
            Class.forName("co.aikar.timings.FullServerTickHandler");
        } catch (Exception e) {
            logger.severe(() -> "==============================" + plugin.getName() + "==============================");
            logger.severe(() -> "");
            logger.severe(() -> "This plugin requires Paper to run.");
            logger.severe(() -> "Please download Paper to run this plugin.");
            logger.severe(() -> "");
            logger.severe(() -> "Paper: https://papermc.io/downloads");
            logger.severe(() -> "");
            logger.severe(() -> "==============================" + plugin.getName() + "==============================");
            logger.severe(() -> "");
            Bukkit.shutdown();
        }
    }

    /**
     * Checks weather this server has a version which is not older than the configured.
     *
     * @param required The required version.
     */
    private void requiresVersion(@Nullable Version required) {
        if (required == null) {
            logger.severe(() -> "Unable to check the required version.");
            logger.severe(() -> "Please check your config or inform the developer.");
            return;
        }

        Version actualVersion = MinecraftVersion.getServerVersion();

        if (MinecraftVersion.COMPARATOR.compare(required, actualVersion) > 0) {
            logger.severe(() -> "==============================" + plugin.getName() + "==============================");
            logger.severe(() -> "");
            logger.severe(() -> "You need a newer version of Minecraft (" + required + " or above) to run this plugin!");
            logger.severe(() -> "Please download Spigot or Paper to run this plugin.");
            logger.severe(() -> "");
            logger.severe(() -> "Spigot: https://getbukkit.org/download/spigot");
            logger.severe(() -> "Spigot build-tools: https://www.spigotmc.org/wiki/buildtools");
            logger.severe(() -> "Paper: https://papermc.io/downloads");
            logger.severe(() -> "");
            logger.severe(() -> "==============================" + plugin.getName() + "==============================");
            logger.severe(() -> "");
            Bukkit.shutdown();
        }
    }

    /**
     * Helper method to check weather the plugin needs a requirement.
     *
     * @param requirement The path to the requirement.
     * @return The result of the check.
     */
    private boolean getRequirement(@NotNull String requirement, boolean def) {
        return requirementsConfig.getBoolean(requirement, def);
    }
}
