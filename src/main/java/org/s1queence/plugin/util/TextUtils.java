package org.s1queence.plugin.util;

import org.bukkit.ChatColor;

public class TextUtils {

    public static String getTextWithInsertedTextContent(String text, String textToInsert) {
        return ChatColor.translateAlternateColorCodes('&', text.replace("%content%", textToInsert));
    }
}
