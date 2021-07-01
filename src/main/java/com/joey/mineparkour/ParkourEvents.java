package com.joey.mineparkour;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.joey.mineparkour.Parkour.*;

public class ParkourEvents implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (playingPlayers.containsKey(player)) {
            World world = player.getWorld();
            Location location = player.getLocation();
            Material type = world.getBlockAt(location.add(0, -1, 0)).getType();

            if (type == Material.GOLD_BLOCK) {
                world.spawnEntity(location, EntityType.FIREWORK);
                player.sendTitle(ChatColor.BOLD + "Congratulation!", player.getName() + " succeeded the map");

                player.setGameMode(playerWithGameMode.get(player));

                playingPlayers.remove(player);
                spawnPoint.remove(player);
                playersWithColor.remove(player);
                playerWithGameMode.remove(player);

                shulkers.forEach((name, shulker) -> {
                    shulkers.remove(name);
                    shulker.remove();
                });
            }
            else if (type == Material.STONECUTTER) {
                player.teleport(spawnPoint.get(player));
            }


        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();
            if (block.getType().toString().contains("_BANNER")) {
                String material = colorNames.get(playersWithColor.get(player)) + "_BANNER";
                BlockFace face = block.getFace(block);

                block.setType(Material.getMaterial(material));

                spawnPoint.replace(player, block.getLocation());
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.SHULKER) {
            if (!playingPlayers.isEmpty()) {
                e.setCancelled(true);
            }
        }
    }

}
