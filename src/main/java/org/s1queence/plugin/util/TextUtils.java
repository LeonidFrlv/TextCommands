package org.s1queence.plugin.util;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.ChatColor;
import org.s1queence.plugin.TextCommands;


public class TextUtils {
    public static String getTextFromTextConfig(String path, TextCommands plugin) {
        YamlDocument config = plugin.getTextConfig();
        String msg = config.getString(path);
        String title = "[" + ChatColor.GOLD + "TextCommands" + ChatColor.WHITE + "]";

        if (msg == null)  {
            String nullMsgError = "&6%plugin% FATAL ERROR." + " We recommend that you delete the text.yml file from the plugin folder and use reload config.";
            return ChatColor.translateAlternateColorCodes('&', nullMsgError.replace("%plugin%", title).replace("%msg_path%", path));
        }

        return (ChatColor.translateAlternateColorCodes('&', msg.replace("%plugin%", title)));
    }


    public static String getTextWithInsertedTextContent(String text, String textToInsert) {
        return ChatColor.translateAlternateColorCodes('&', text.replace("%content%", textToInsert));
    }
}
