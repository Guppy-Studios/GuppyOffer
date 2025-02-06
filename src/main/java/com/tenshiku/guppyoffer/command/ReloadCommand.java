package com.tenshiku.guppyoffer.command;

import com.tenshiku.guppyoffer.GuppyOffer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    private final GuppyOffer plugin;

    public ReloadCommand(GuppyOffer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("guppyoffer.reload")) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        plugin.reloadConfig();
        plugin.reloadPlugin();
        sender.sendMessage("§aGuppyOffer configuration reloaded successfully!");
        return true;
    }
}