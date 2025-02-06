package com.tenshiku.guppyoffer.listener;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.tenshiku.guppyoffer.GuppyOffer;
import com.tenshiku.guppyoffer.config.ConfigManager;
import com.tenshiku.guppyoffer.config.MessagesManager;
import com.tenshiku.guppyoffer.manager.OfferingManager;
import com.tenshiku.guppyoffer.model.Reward;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class DropListener implements Listener {
    private final GuppyOffer plugin;
    private final ConfigManager configManager;
    private final OfferingManager offeringManager;
    private final MessagesManager messagesManager;

    public DropListener(GuppyOffer plugin, ConfigManager configManager, OfferingManager offeringManager, MessagesManager messagesManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.offeringManager = offeringManager;
        this.messagesManager = messagesManager;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!configManager.isValidWorld(world)) return;

        String regionName = configManager.getRegionName(world);
        if (regionName == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!item.isValid()) return;

                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager regions = container.get(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(world));
                if (regions == null) return;

                com.sk89q.worldedit.util.Location loc = com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(item.getLocation());
                ApplicableRegionSet set = regions.getApplicableRegions(loc.toVector().toBlockPoint());

                ProtectedRegion highestRegion = null;
                int highestPriority = Integer.MIN_VALUE;

                for (ProtectedRegion region : set) {
                    if (region.getPriority() > highestPriority) {
                        highestPriority = region.getPriority();
                        highestRegion = region;
                    }
                }

                if (highestRegion != null && highestRegion.getId().equals(regionName)) {
                    if (offeringManager.isValidOffering(item.getItemStack().getType())) {
                        item.remove();
                        Reward reward = offeringManager.giveReward(player);
                        if (reward != null) {
                            messagesManager.sendMessage(player, "reward-given",
                                    "{item-name}", reward.getName());
                            // Play accepted sound
                            String sound = plugin.getConfig().getString("sounds.accepted-offering.sound", "ENTITY_PLAYER_LEVELUP");
                            float volume = (float) plugin.getConfig().getDouble("sounds.accepted-offering.volume", 1.0);
                            float pitch = (float) plugin.getConfig().getDouble("sounds.accepted-offering.pitch", 1.0);
                            player.playSound(player.getLocation(), sound, volume, pitch);
                        }
                    } else {
                        // Play rejected sound
                        String sound = plugin.getConfig().getString("sounds.rejected-offering.sound", "BLOCK_ANVIL_LAND");
                        float volume = (float) plugin.getConfig().getDouble("sounds.rejected-offering.volume", 1.0);
                        float pitch = (float) plugin.getConfig().getDouble("sounds.rejected-offering.pitch", 0.5);
                        player.playSound(player.getLocation(), sound, volume, pitch);
                        messagesManager.sendMessage(player, "invalid-offering");
                        // Return the exact item to the player's inventory
                        player.getInventory().addItem(item.getItemStack());
                        item.remove();
                    }
                }
            }
        }.runTaskLater(player.getServer().getPluginManager().getPlugin("GuppyOffer"), 10L);
    }
}