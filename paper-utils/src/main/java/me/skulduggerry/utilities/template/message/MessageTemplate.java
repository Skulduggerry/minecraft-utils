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

package me.skulduggerry.utilities.template.message;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface which represents the template for a message.
 * Allows different message for every receiver.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
@FunctionalInterface
public interface MessageTemplate {

    /**
     * Get a message template which gives the same message for every receiver.
     * Allows color codes with the {@link MiniMessage}-format
     *
     * @param message The message.
     * @return The template.
     */
    static MessageTemplate of(@NotNull String message) {
        return (receiver, args) -> MiniMessage.miniMessage().deserialize(message);
    }

    /**
     * Get the message for the given receiver.
     * the receiver should not be an {@link ForwardingAudience}.
     *
     * @param receiver Receiver to get the message for
     * @param args     Format arguments.
     * @return The message.
     */
    Component getMessage(@NotNull Audience receiver, Object @NotNull ... args);

    /**
     * Send the message to the given receiver.
     *
     * @param receiver The receiver to send the message.
     * @param args     Format arguments.
     */
    default void send(@NotNull Audience receiver, Object @NotNull ... args) {
        if (receiver instanceof ForwardingAudience forwardingAudience) {
            receiver.forEachAudience(audience -> send(audience, args));
            return;
        }

        Component message = getMessage(receiver, args);
        receiver.sendMessage(message);
    }

    /**
     * Sends a title and a subtitle to the receiver.
     * Its times will be {@link Title#DEFAULT_TIMES}
     *
     * @param receiver The receiver of the title
     * @param subTitle The subtitle.
     */
    default void title(@NotNull Audience receiver, @Nullable MessageTemplate subTitle) {
        title(receiver, subTitle, Title.DEFAULT_TIMES, new Object[]{}, new Object[]{});
    }

    /**
     * Sends a title and a subtitle to the receiver.
     * Its times will be {@link Title#DEFAULT_TIMES}
     *
     * @param receiver The receiver of the title
     * @param subTitle The subtitle.
     * @param times    The times of the title.
     */
    default void title(@NotNull Audience receiver, @Nullable MessageTemplate subTitle, @NotNull Title.Times times) {
        title(receiver, subTitle, times, new Object[]{}, new Object[]{});
    }

    /**
     * Sends a title and a subtitle to the receiver.
     * Its times will be {@link Title#DEFAULT_TIMES}
     *
     * @param receiver     The receiver of the title
     * @param subTitle     The subtitle.
     * @param times        The times of the title.
     * @param args         The format arguments for the title
     * @param subTitleArgs The format arguments for the subtitle.
     */
    default void title(@NotNull Audience receiver, @Nullable MessageTemplate subTitle, @NotNull Title.Times times, Object @NotNull [] args, Object @NotNull [] subTitleArgs) {
        if (receiver instanceof ForwardingAudience forwardingAudience) {
            forwardingAudience.forEachAudience(audience -> title(audience, subTitle, times, args, subTitleArgs));
            return;
        }

        Component titleText = getMessage(receiver, args);
        Component subTitleText = subTitle != null ? subTitle.getMessage(receiver, subTitleArgs) : Component.empty();
        Title title = Title.title(titleText, subTitleText, times);
        receiver.showTitle(title);
    }

    /**
     * Sends an Action Bar message to the client.
     * <p>
     * Use Section symbols for legacy color codes to send formatting.
     *
     * @param receiver Receiver to show the action bar
     * @param args     The format arguments.
     */
    default void actionBar(@NotNull Audience receiver, @NotNull Object... args) {
        if (receiver instanceof ForwardingAudience forwardingAudience) {
            forwardingAudience.forEachAudience(audience -> actionBar(audience, args));
            return;
        }

        Component text = getMessage(receiver, args);
        receiver.sendActionBar(text);
    }

    /**
     * Returns a new message template with a prefix.
     * Automatically adds brackets around the prefix.
     *
     * @param prefix The prefix.
     * @return The new template
     */
    default MessageTemplate withPrefix(@NotNull MessageTemplate prefix) {
        return withPrefix(prefix, this);
    }

    /**
     * Returns a new message template with a prefix.
     * Automatically adds brackets around the prefix.
     * The prefix will not receive any format arguments from the message template.
     *
     * @param prefix  The prefix.
     * @param message The message
     * @return The new template.
     */
    static MessageTemplate withPrefix(@NotNull MessageTemplate prefix, @NotNull MessageTemplate message) {
        return (receiver, args) -> Component.text("[").color(NamedTextColor.GRAY)
                .append(prefix.getMessage(receiver))
                .append(Component.text("]").color(NamedTextColor.GRAY))
                .append(Component.space())
                .append(message.getMessage(receiver, args));
    }
}
