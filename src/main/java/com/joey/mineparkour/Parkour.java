package com.joey.mineparkour;

import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Parkour {
    public static Map<String, Region> maps = new HashMap<>();
    public static Map<Player, String> playingPlayers = new HashMap<>();
    public static Map<Player, Location> spawnPoint = new HashMap<>();
    public static Map<Player, Material> playersWithColor = new HashMap<>();

}
