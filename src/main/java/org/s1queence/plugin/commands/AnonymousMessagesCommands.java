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

public class AnonymousMessagesCommands implements CommandExecutor {
    private final TextCommands plugin;
    public AnonymousMessagesCommands(TextCommands plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length == 0) return false;
        Player player = (Player) sender;
        StringBuilder msg = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            msg.append(args[i]).append(" ");
        }

        msg = new StringBuilder(ChatColor.translateAlternateColorCodes('&', msg.toString()));

        if (command.getName().equalsIgnoreCase("anonsay")) {
            if (!plugin.isAnonSayCommand()) {
                player.sendMessage(ChatColor.RED + "Команда выключена!");
                return true;
            }
            List<Entity> nearbyEntities = player.getNearbyEntities(25, 25, 25);

            int receiversCounter = 0;
            for (Entity entity : nearbyEntities) {
                if (!(entity instanceof Player)) continue;
                Player p = (Player) entity;
                p.sendMessage(msg.toString());
                p.playSound(p.getLocation(), "custom.subtitle_sound", 1.0f, 1.0f);
                receiversCounter++;
            }

            if (receiversCounter == 0) {
                player.sendMessage(ChatColor.GRAY + "Никто этого не увидел...");
                return true;
            }

            player.sendMessage(msg.toString());

            return true;
        }

        if (command.getName().equalsIgnoreCase("anonsilentsay")) {
            if (!plugin.isAnonSilentSayCommand()) {
                player.sendMessage(ChatColor.RED + "Команда выключена!");
                return true;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(ChatColor.DARK_RED + "Игрок не найден!");
                return true;
            }

            target.sendMessage(msg.toString());
            player.sendMessage(ChatColor.GRAY + "<Вы -> " + target.getName() + ">: " + msg);
        }

        return true;
    }
}
