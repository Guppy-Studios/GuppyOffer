package com.tenshiku.guppyoffer.manager;

import com.tenshiku.guppyoffer.GuppyOffer;
import com.tenshiku.guppyoffer.model.Offering;
import com.tenshiku.guppyoffer.model.Reward;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class OfferingManager {
    private final GuppyOffer plugin;
    private final Map<Material, Offering> offerings;
    private final List<Reward> rewards;
    private final Random random;

    public OfferingManager(GuppyOffer plugin) {
        this.plugin = plugin;
        this.offerings = new HashMap<>();
        this.rewards = new ArrayList<>();
        this.random = new Random();
        loadOfferings();
        loadRewards();
    }

    private void loadOfferings() {
        ConfigurationSection offeringsSection = plugin.getConfig().getConfigurationSection("offerings");
        if (offeringsSection == null) return;

        for (String key : offeringsSection.getKeys(false)) {
            Material material;
            try {
                material = Material.valueOf(key.toUpperCase());
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid material name in config: " + key);
                continue;
            }
            offerings.put(material, new Offering(material));
        }
    }

    private void loadRewards() {
        ConfigurationSection rewardsSection = plugin.getConfig().getConfigurationSection("rewards");
        if (rewardsSection == null) return;

        for (String key : rewardsSection.getKeys(false)) {
            String name = rewardsSection.getString(key + ".name", key);
            String command = rewardsSection.getString(key + ".command");
            int chance = rewardsSection.getInt(key + ".chance");

            rewards.add(new Reward(key, name, command, chance));
        }
    }

    public boolean isValidOffering(Material material) {
        return offerings.containsKey(material);
    }

    public Reward giveReward(Player player) {
        int totalChance = rewards.stream()
                .mapToInt(Reward::getChance)
                .sum();

        int roll = random.nextInt(totalChance);
        int currentSum = 0;

        for (Reward reward : rewards) {
            currentSum += reward.getChance();
            if (roll < currentSum) {
                String command = reward.getCommand().replace("{player}", player.getName());
                plugin.getServer().dispatchCommand(
                        plugin.getServer().getConsoleSender(),
                        command
                );
                return reward;
            }
        }

        return null;
    }
}