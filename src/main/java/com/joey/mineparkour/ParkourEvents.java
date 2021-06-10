package com.joey.mineparkour;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.joey.mineparkour.Parkour.playingPlayers;
import static com.joey.mineparkour.Parkour.spawnPoint;

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

                playingPlayers.remove(player);
            }
            else if (type == Material.STONECUTTER) {
                player.teleport(spawnPoint.get(player));
            }
        }
    }

}
