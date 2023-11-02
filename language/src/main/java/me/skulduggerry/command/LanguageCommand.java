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

package me.skulduggerry.command;

import me.skulduggerry.Language;
import me.skulduggerry.LanguageManager;
import me.skulduggerry.Main;
import me.skulduggerry.utilities.command.PlayerCommand;
import me.skulduggerry.utilities.template.message.MessageTemplate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class LanguageCommand implements PlayerCommand {

    public final MessageTemplate PREFIX;
    public final MessageTemplate CHANGED;
    public final MessageTemplate NOT_FOUND;
    public final MessageTemplate HELP;
    public final MessageTemplate PLAYER_COMMAND;
    private final LanguageManager languageManager;

    /**
     * Constructor
     *
     * @param languageManager Different messages for every player
     */
    public LanguageCommand(@NotNull LanguageManager languageManager) {
        this.languageManager = languageManager;

        PREFIX = languageManager.toMessageTemplate("command.language.prefix");
        CHANGED = languageManager.toMessageTemplate("command.language.changed").withPrefix(PREFIX);
        NOT_FOUND = languageManager.toMessageTemplate("command.language.not-found").withPrefix(PREFIX);
        HELP = languageManager.toMessageTemplate("command.language.help").withPrefix(PREFIX);
        PLAYER_COMMAND = languageManager.toMessageTemplate("command.player-command").withPrefix(PREFIX);
    }

    /**
     * Send help to player if he used the wrong syntax.
     *
     * @param sender the sender
     */
    @Override
    public void showHelp(@NotNull CommandSender sender) {
        HELP.send(sender);
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) {
            showHelp(sender);
            return true;
        }

        if (args.length == 0) {
            Main.getInstance().getUiManager().getMenu().open(sender);
            return true;
        }

        for (Language language : languageManager.getAvailableLanguages()) {
            Locale locale = language.getLocale();
            if (locale.getDisplayLanguage(locale).equalsIgnoreCase(args[0])) {
                languageManager.setPlayerLanguage(sender, locale);
                CHANGED.send(sender, locale.getDisplayLanguage(locale));
                return true;
            }
        }

        NOT_FOUND.send(sender);
        return true;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return List.of();

        return languageManager.getAvailableLanguages().stream()
                .map(Language::getLocale)
                .map(locale -> locale.getDisplayLanguage(locale))
                .toList();
    }

    /**
     * Sends a message that this command can only be used by players.
     *
     * @param sender The sender who is not a player
     */
    @Override
    public void sendPlayerCommand(CommandSender sender) {
        PLAYER_COMMAND.send(sender);
    }
}
