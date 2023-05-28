package me.skulduggerry.utilities.template.title;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Interface which represents the template for a message.
 * Allows different message for every receiver.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
@FunctionalInterface
public interface PageTitleTemplate {

    /**
     * Get a message template which gives the same message for every player and page.
     *
     * @param title The title.
     * @return The template.
     */
    static PageTitleTemplate of(@NotNull String title) {
        return (receiver, page) -> LegacyComponentSerializer.legacySection().deserialize(title);
    }

    /**
     * Get a message template which gives the same message for every player but depends on the page number.
     *
     * @param title The title.
     * @return The template.
     */
    static PageTitleTemplate ofNumbered(@NotNull String title) {
        return (receiver, page) -> LegacyComponentSerializer.legacySection().deserialize(title)
                .append(Component.text(" | ").color(NamedTextColor.GRAY))
                .append(Component.text(page).color(NamedTextColor.BLACK));
    }

    /**
     * Get the message for the given player.
     * Every player can get a unique message.
     *
     * @param receiver The player.
     * @return The message.
     */
    Component getTitle(@NotNull Player receiver, @Range(from = 1, to = Integer.MAX_VALUE) int page);
}
