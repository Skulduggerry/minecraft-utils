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

import me.skulduggerry.utilities.manager.Manager;
import me.skulduggerry.utilities.menu.Menu;
import me.skulduggerry.utilities.menu.mask.Mask;
import me.skulduggerry.utilities.menu.mask.type.SimpleMask;
import me.skulduggerry.utilities.menu.page.Page;
import me.skulduggerry.utilities.menu.page.type.ChestPage;
import me.skulduggerry.utilities.menu.slot.SlotSettings;
import me.skulduggerry.utilities.menu.type.SimpleMenu;
import me.skulduggerry.utilities.template.item.ItemTemplate;

import java.util.Arrays;
import java.util.Locale;

public class UiManager implements Manager {

    private static UiManager instance;

    public static UiManager getInstance() {
        return instance != null ? instance : (instance = new UiManager());
    }

    private Menu menu;

    /**
     * Constructor
     */
    private UiManager() {
    }

    /**
     * Called when the manager gets enabled
     */
    @Override
    public void handleEnable() {

    }

    /**
     * called when the manager gets disabled
     */
    @Override
    public void handleDisable() {

    }

    public Menu getMenu() {
        if (menu != null) return menu;
        createMenu();
        return menu;
    }

    public void updateMenu() {
        createMenu();
    }

    private void createMenu() {
        ItemTemplate[] maskItems = new ItemTemplate[9];
        Arrays.fill(maskItems, ItemTemplate.of(Items.BLUE_GLASS));
        Mask mask = SimpleMask.builder(4)
                .set(maskItems)
                .lastRow()
                .set(maskItems)
                .build();

        Page.Builder<?> pageBuilder = ChestPage.builder(4)
                .title((receiver, page) -> LanguageAPI.getMessage(receiver, "ui.lang.title"));

        menu = SimpleMenu.builder(pageBuilder)
                .mask(mask)
                .closePageItem(8, Items.CLOSE_PAGE_ITEM)
                .build();
        applyMenuItems(menu);
    }

    private void applyMenuItems(Menu menu) {
        for (Language language : Main.getInstance().getLanguageManager().getAvailableLanguages()) {
            Locale locale = language.getLocale();
            menu.addItem(SlotSettings.builder()
                    .template(ItemTemplate.of(language.getItem()))
                    .handler(information -> {
                        information.player().performCommand("language " + locale.getDisplayLanguage(locale));
                        menu.updateFull();
                    })
                    .build()
            );
        }
    }
}
