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

package me.skulduggerry;

import me.skulduggerry.impl.ConfigBasedLanguageManager;
import me.skulduggerry.utilities.ExtendedPlugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

/**
 * Main plugin class
 *
 * @author Skulduggerry
 * @since 1.0.0
 */
public class Main extends ExtendedPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    /**
     * Constructor
     */
    public Main() {
        if (instance == null) {
            instance = this;
        }
    }

    private LanguageManager languageManager;
    private UiManager uiManager;

    /**
     * Create the managers.
     */
    @Override
    protected void createManagers() {
        super.createManagers();
        languageManager = ConfigBasedLanguageManager.getInstance(this, true);
        uiManager = UiManager.getInstance();
    }

    /**
     * Enable the managers.
     */
    @Override
    protected void enableManagers() {
        super.enableManagers();
        languageManager.handleEnable();
        uiManager.handleEnable();
    }

    /**
     * Registers all {@link CommandExecutor}s.
     */
    @Override
    protected void registerCommands() {
        super.registerCommands();
    }

    /**
     * Registers all {@link Listener}s.
     */
    @Override
    protected void registerListeners() {
        super.registerListeners();
    }

    /**
     * Disable the managers.
     */
    @Override
    protected void disableManagers() {
        uiManager.handleDisable();
        languageManager.handleDisable();

        //call after all other managers because some managers uses the functionality of others
        super.disableManagers();
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public UiManager getUiManager() {
        return uiManager;
    }
}
