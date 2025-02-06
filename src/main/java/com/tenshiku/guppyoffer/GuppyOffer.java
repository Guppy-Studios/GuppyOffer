package com.tenshiku.guppyoffer;

import com.tenshiku.guppyoffer.command.ReloadCommand;
import com.tenshiku.guppyoffer.config.ConfigManager;
import com.tenshiku.guppyoffer.config.MessagesManager;
import com.tenshiku.guppyoffer.listener.DropListener;
import com.tenshiku.guppyoffer.manager.OfferingManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuppyOffer extends JavaPlugin {
    private ConfigManager configManager;
    private MessagesManager messagesManager;
    private OfferingManager offeringManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadManagers();
        registerListeners();
        registerCommands();
    }

    private void loadManagers() {
        configManager = new ConfigManager(this);
        messagesManager = new MessagesManager(this);
        offeringManager = new OfferingManager(this);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(
                new DropListener(this, configManager, offeringManager, messagesManager),
                this
        );
    }

    private void registerCommands() {
        getCommand("guppyoffer").setExecutor(new ReloadCommand(this));
    }

    public void reloadPlugin() {
        loadManagers();
    }

    @Override
    public void onDisable() {
        getLogger().info("GuppyOffer has been disabled!");
    }
}
