package com.tenshiku.guppyoffer.config;

import com.tenshiku.guppyoffer.GuppyOffer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final GuppyOffer plugin;
    private final Map<String, String> regionNames;

    public ConfigManager(GuppyOffer plugin) {
        this.plugin = plugin;
        this.regionNames = new HashMap<>();
        loadConfig();
    }

    private void loadConfig() {
        ConfigurationSection worlds = plugin.getConfig().getConfigurationSection("worlds");
        if (worlds == null) return;

        for (String worldName : worlds.getKeys(false)) {
            ConfigurationSection worldSection = worlds.getConfigurationSection(worldName);
            if (worldSection == null) continue;

            String regionName = worldSection.getString("region");

            if (regionName != null) {
                regionNames.put(worldName, regionName);
            }
        }
    }

    public String getRegionName(World world) {
        return regionNames.get(world.getName());
    }

    public boolean isValidWorld(World world) {
        return regionNames.containsKey(world.getName());
    }
}