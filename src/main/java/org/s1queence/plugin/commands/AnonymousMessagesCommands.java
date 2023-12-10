package org.s1queence.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.s1queence.plugin.TextCommands;

import java.util.List;

import static org.s1queence.api.S1TextUtils.getTextWithInsertedPlayerName;
import static org.s1queence.plugin.util.TextUtils.getTextFromTextConfig;
import static org.s1queence.plugin.util.TextUtils.getTextWithInsertedTextContent;

public class AnonymousMessagesCommands implements CommandExecutor {
    private final TextCommands plugin;
    public AnonymousMessagesCommands(TextCommands plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (!player.hasPermission("tc.perms.anonsay")) {
            player.sendMessage(getTextFromTextConfig("no_perm", plugin));
            return true;
        }
        if (args.length == 0) return false;
        StringBuilder msg = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            msg.append(args[i]).append(" ");
        }

        msg = new StringBuilder(ChatColor.translateAlternateColorCodes('&', msg.toString()));

        if (command.getName().equalsIgnoreCase("anonsay")) {
            if (!plugin.isAnonSayCommand()) {
                player.sendMessage(getTextFromTextConfig("command_disabled_msg", plugin));
                return true;
            }
            List<Entity> nearbyEntities = player.getNearbyEntities(25, 25, 25);

            int receiversCounter = 0;
            for (Entity entity : nearbyEntities) {
                if (!(entity instanceof Player)) continue;
                Player p = (Player) entity;
                p.sendMessage(msg.toString());
                String sound = plugin.getCommandOptionsCfg().getString("sound.anonsay");
                if (sound != null && !sound.equalsIgnoreCase("none")) p.playSound(p.getLocation(), sound, 1.0f, 1.0f);
                receiversCounter++;
            }

            if (receiversCounter == 0) {
                player.sendMessage(getTextFromTextConfig("anonsilentsay.no_one_saw_msg", plugin));
                return true;
            }

            player.sendMessage(msg.toString());

            return true;
        }

        if (command.getName().equalsIgnoreCase("anonsilentsay")) {
            if (!plugin.isAnonSilentSayCommand()) {
                player.sendMessage(getTextFromTextConfig("command_disabled_msg", plugin));
                return true;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(getTextFromTextConfig("player_not_found", plugin));
                return true;
            }

            String sound = plugin.getCommandOptionsCfg().getString("sound.anonsilentsay");
            if (sound != null && !sound.equalsIgnoreCase("none")) target.playSound(target.getLocation(), sound, 1.0f, 1.0f);

            String targetMsg = getTextWithInsertedTextContent(getTextWithInsertedPlayerName(getTextFromTextConfig("anonsilentsay.player_msg", plugin), player.getName()), msg.toString());
            target.sendMessage(targetMsg);

            String senderMsg = getTextWithInsertedTextContent(getTextWithInsertedPlayerName(getTextFromTextConfig("anonsilentsay.sender_msg", plugin), target.getName()), msg.toString());
            player.sendMessage(senderMsg);
        }

        return true;
    }
}
