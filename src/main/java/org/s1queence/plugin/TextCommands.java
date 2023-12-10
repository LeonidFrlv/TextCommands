package org.s1queence.plugin;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.s1queence.plugin.commands.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.s1queence.api.S1TextUtils.consoleLog;
import static org.s1queence.plugin.util.TextUtils.getTextFromTextConfig;

public final class TextCommands extends JavaPlugin implements CommandExecutor {
    private boolean me_command;
    private boolean try_command;
    private boolean do_command;
    private boolean foradmins_command;
    private boolean trouble_command;
    private boolean anonsay_command;
    private boolean anonsilentsay_command;
    private YamlDocument commandOptionsCfg;
    private YamlDocument textCfg;

    @Override
    public void onEnable() {
        try {
            commandOptionsCfg = YamlDocument.create(new File(getDataFolder(), "commands.options.yml"), Objects.requireNonNull(getResource("commands.options.yml")));
            textCfg = YamlDocument.create(new File(getDataFolder(), "text.yml"), Objects.requireNonNull(getResource("text.yml")));
        } catch (IOException ignored) {

        }

        consoleLog( getTextFromTextConfig("enable_msg", this), this);

        Objects.requireNonNull(getServer().getPluginCommand("me")).setExecutor(new RpCommands(this));
        Objects.requireNonNull(getServer().getPluginCommand("do")).setExecutor(new RpCommands(this));
        Objects.requireNonNull(getServer().getPluginCommand("try")).setExecutor(new RpCommands(this));
        Objects.requireNonNull(getServer().getPluginCommand("trouble")).setExecutor(new RpCommands(this));
        Objects.requireNonNull(getServer().getPluginCommand("foradmins")).setExecutor(new RpCommands(this));
        Objects.requireNonNull(getServer().getPluginCommand("anonsay")).setExecutor(new AnonymousMessagesCommands(this));
        Objects.requireNonNull(getServer().getPluginCommand("anonsilentsay")).setExecutor(new AnonymousMessagesCommands(this));
        Objects.requireNonNull(getServer().getPluginCommand("textcommands")).setExecutor(this);

        me_command = commandOptionsCfg.getBoolean("commands_enabler.me");
        try_command = commandOptionsCfg.getBoolean("commands_enabler.try");
        do_command = commandOptionsCfg.getBoolean("commands_enabler.do");
        anonsay_command = commandOptionsCfg.getBoolean("commands_enabler.anonsay");
        anonsilentsay_command = commandOptionsCfg.getBoolean("commands_enabler.anonsilentsay");
        trouble_command = commandOptionsCfg.getBoolean("commands_enabler.trouble");
        foradmins_command = commandOptionsCfg.getBoolean("commands_enabler.foradmins");
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return false;
        if (!args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.RED + getTextFromTextConfig("only_reload_msg", this));
            return true;
        }

        if (sender instanceof Player) {
            if (!sender.hasPermission("tc.perms.reload")) {
                sender.sendMessage(getTextFromTextConfig("no_perm", this));
                return true;
            }
        }

        try {
            File commandOptionsCfgFile = new File(getDataFolder(), "commands.options.yml");
            File textCfgFile = new File(getDataFolder(), "text.yml");
            if (!commandOptionsCfgFile.exists()) commandOptionsCfg = YamlDocument.create(new File(getDataFolder(), "commands.options.yml"), Objects.requireNonNull(getResource("commands.options.yml")));
            if (!textCfgFile.exists()) textCfg = YamlDocument.create(new File(getDataFolder(), "text.yml"), Objects.requireNonNull(getResource("text.yml")));
            commandOptionsCfg.reload();
            textCfg.reload();
        } catch (IOException ignored) {

        }

        me_command = commandOptionsCfg.getBoolean("commands_enabler.me");
        try_command = commandOptionsCfg.getBoolean("commands_enabler.try");
        do_command = commandOptionsCfg.getBoolean("commands_enabler.do");
        anonsay_command = commandOptionsCfg.getBoolean("commands_enabler.anonsay");
        anonsilentsay_command = commandOptionsCfg.getBoolean("commands_enabler.anonsilentsay");
        trouble_command = commandOptionsCfg.getBoolean("commands_enabler.trouble");
        foradmins_command = commandOptionsCfg.getBoolean("commands_enabler.foradmins");

        String reloadMsg = getTextFromTextConfig("reload_msg", this);
        consoleLog(reloadMsg, this);
        if (sender instanceof Player) sender.sendMessage(reloadMsg);
        return true;
    }

    @Override
    public void onDisable() {
        consoleLog( getTextFromTextConfig("disable_msg", this), this);
    }

    public YamlDocument getTextConfig() {return textCfg;}
    public YamlDocument getCommandOptionsCfg() {return commandOptionsCfg;}

    public boolean isDoCommand() {return do_command;}
    public boolean isMeCommand() {return me_command;}
    public boolean isTryCommand() {return try_command;}
    public boolean isTroubleCommand() {return trouble_command;}
    public boolean isForAdminsCommand() {return foradmins_command;}
    public boolean isAnonSayCommand() {return anonsay_command;}
    public boolean isAnonSilentSayCommand() {return anonsilentsay_command;}

}
