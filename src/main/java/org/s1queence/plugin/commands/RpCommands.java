package org.s1queence.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.s1queence.plugin.TextCommands;

import java.util.Collection;
import java.util.List;

import static org.s1queence.api.S1TextUtils.getConvertedTextFromConfig;
import static org.s1queence.api.S1TextUtils.getTextWithInsertedPlayerName;
import static org.s1queence.api.S1Utils.notifyAdminsAboutCommand;
import static org.s1queence.plugin.util.TextUtils.getTextWithInsertedTextContent;

public class RpCommands implements CommandExecutor {
    private final TextCommands plugin;
    public RpCommands(TextCommands plugin) {this.plugin = plugin;}
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length == 0) return false;
        Player player = (Player) sender;
        List<Entity> nearbyEntities = player.getNearbyEntities(25.0d, 25.0d, 12.0d);
        String commandDisabledMsg = getConvertedTextFromConfig(plugin.getTextConfig(), "command_disabled_msg", plugin.getName());

        StringBuilder msg = new StringBuilder();
        for (String s1 : args) {
            msg.append(s1).append(" ");
        }

        String commandName = command.getName().toLowerCase();

        String senderMsg = getTextWithInsertedTextContent(getTextWithInsertedPlayerName(getConvertedTextFromConfig(plugin.getTextConfig(), commandName + ".msg_for_sender", plugin.getName()), sender.getName()), msg.toString());
        String msgForAnotherPlayers = getTextWithInsertedTextContent(getTextWithInsertedPlayerName(getConvertedTextFromConfig(plugin.getTextConfig(), commandName + ".msg_for_another_players", plugin.getName()), sender.getName()), msg.toString());

        Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();

        if (commandName.equals("me")) {
            if (!plugin.isMeCommand()) {
                player.sendMessage(commandDisabledMsg);
                return true;
            }
        }

        if (commandName.equals("do")) {
            if (!plugin.isDoCommand()) {
                player.sendMessage(commandDisabledMsg);
                return true;
            }
        }

        if (commandName.equals("try")) {
            if (!plugin.isTryCommand()) {
                player.sendMessage(commandDisabledMsg);
                return true;
            }
            String resultText = (int)(Math.random() * 2) == 1 ? getConvertedTextFromConfig(plugin.getTextConfig(), "try.result_positive", plugin.getName()) : getConvertedTextFromConfig(plugin.getTextConfig(), "try.result_negative", plugin.getName());
            senderMsg = senderMsg.replace("%result%", resultText);
            msgForAnotherPlayers = msgForAnotherPlayers.replace("%result%", resultText);
        }


        if (commandName.equals("trouble")) {
            if (!plugin.isTroubleCommand()) {
                player.sendMessage(commandDisabledMsg);
                return true;
            }
        }

        if (commandName.equals("foradmins")) {
            if (!plugin.isForAdminsCommand()) {
                player.sendMessage(commandDisabledMsg);
                return true;
            }
            player.sendMessage(getConvertedTextFromConfig(plugin.getTextConfig(), "foradmins.sender_msg", plugin.getName()));
            notifyAdminsAboutCommand(onlinePlayers, command.getName(), msg.toString(), player.getName(), null);
            return true;
        }

        player.sendMessage(senderMsg);

        notifyAdminsAboutCommand(onlinePlayers, command.getName(), msgForAnotherPlayers, player.getName(), null);

        if (nearbyEntities.size() == 0) return true;

        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof Player)) continue;
            Player p = (Player) entity;
            if (p.hasPermission("tc.perms.log")) continue;
            p.sendMessage(msgForAnotherPlayers);
            String sound = plugin.getCommandOptionsCfg().getString("sound." + commandName);
            if (sound != null && !sound.equalsIgnoreCase("none")) p.playSound(p.getLocation(), sound, 1.0f, 1.0f);
        }

        return true;
    }
}
