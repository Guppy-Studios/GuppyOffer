package com.tenshiku.guppyoffer.config;

import com.tenshiku.guppyoffer.GuppyOffer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class MessagesManager {
    private final GuppyOffer plugin;
    private final MiniMessage miniMessage;
    private YamlConfiguration messages;

    public MessagesManager(GuppyOffer plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        loadMessages();
    }

    private void loadMessages() {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            try (InputStream in = plugin.getResource("messages.yml")) {
                Files.copy(in, messagesFile.toPath());
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create messages.yml");
                return;
            }
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void sendMessage(Player player, String key, String... replacements) {
        String message = messages.getString("messages." + key);
        if (message == null) return;

        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }

        Component component = miniMessage.deserialize(message);
        player.sendMessage(component);
    }
}