package org.s1queence.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.s1queence.plugin.TextCommands;

import java.util.Collection;
import java.util.List;

import static org.s1queence.api.S1Utils.notifyAdminsAboutCommand;

public class RpCommands implements CommandExecutor {
    private final TextCommands plugin;
    public RpCommands(TextCommands plugin) {this.plugin = plugin;}
    private String getColoredSymbol(String symbol) {
        return ChatColor.GOLD + symbol;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length == 0) return false;
        Player player = (Player) sender;
        List<Entity> nearbyEntities = player.getNearbyEntities(25.0d, 25.0d, 12.0d);

        StringBuilder msg = new StringBuilder();
        for (String s1 : args) {
            msg.append(" ").append(s1);
        }

        String playerMsg = "";
        String finalMsg = "";

        Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();

        if (command.getName().equalsIgnoreCase("me")) {
            if (!plugin.isMeCommand()) {
                player.sendMessage(ChatColor.RED + "Команда выключена!");
                return true;
            }
            playerMsg = getColoredSymbol("* ") + ChatColor.WHITE + player.getName() + getColoredSymbol(" »") + ChatColor.WHITE + msg;
            finalMsg = getColoredSymbol("* ") + ChatColor.GRAY + player.getName() + getColoredSymbol(" »") + ChatColor.GRAY + msg;
        }

        if (command.getName().equalsIgnoreCase("do")) {
            if (!plugin.isDoCommand()) {
                player.sendMessage(ChatColor.RED + "Команда выключена!");
                return true;
            }
            playerMsg = getColoredSymbol("^ ") + ChatColor.WHITE + player.getName() + getColoredSymbol(" »") + ChatColor.WHITE + msg;
            finalMsg = getColoredSymbol("^ ") + ChatColor.GRAY + player.getName() + getColoredSymbol(" »") + ChatColor.GRAY + msg;
        }

        if (command.getName().equalsIgnoreCase("try")) {
            if (!plugin.isTryCommand()) {
                player.sendMessage(ChatColor.RED + "Команда выключена!");
                return true;
            }
            String result = ChatColor.GRAY + " (" + ((int)(Math.random() * 2) == 1 ? ChatColor.DARK_GREEN + "Успешно" : ChatColor.DARK_RED + "Неуспешно") + ChatColor.GRAY + ")";
            playerMsg = getColoredSymbol("% ") + ChatColor.WHITE + player.getName() + getColoredSymbol(" »") + ChatColor.WHITE + msg + result;
            finalMsg = getColoredSymbol("% ") + ChatColor.GRAY + player.getName() + getColoredSymbol(" »") + ChatColor.GRAY + msg + result;
        }


        if (command.getName().equalsIgnoreCase("trouble")) {
            if (!plugin.isTroubleCommand()) {
                player.sendMessage(ChatColor.RED + "Команда выключена!");
                return true;
            }
            playerMsg = getColoredSymbol("! ") + ChatColor.WHITE + player.getName() + getColoredSymbol(" »") + ChatColor.WHITE + msg;
            finalMsg = getColoredSymbol("! ") + ChatColor.GRAY + player.getName() + getColoredSymbol(" »") + ChatColor.GRAY + msg;
        }

        if (command.getName().equalsIgnoreCase("foradmins")) {
            if (!plugin.isForAdminsCommand()) {
                player.sendMessage(ChatColor.RED + "Команда выключена!");
                return true;
            }
            player.sendMessage( ChatColor.DARK_GREEN + "Администрация была уведомлена. " + ChatColor.GRAY + "Ждите...");
            notifyAdminsAboutCommand(onlinePlayers, command.getName(), msg.toString(), player.getName(), null);
            return true;
        }

        player.sendMessage(playerMsg);
        notifyAdminsAboutCommand(onlinePlayers, command.getName(), finalMsg, player.getName(), null);

        if (nearbyEntities.size() == 0) return true;

        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof Player)) continue;
            Player p = (Player) entity;
            if (p.hasPermission("st.perms.log")) continue;
            p.sendMessage(finalMsg);
            p.playSound(p.getLocation(), "custom.subtitle_sound", 1.0f, 1.0f);
        }

        return true;
    }
}
