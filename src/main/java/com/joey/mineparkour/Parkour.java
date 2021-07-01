package com.joey.mineparkour;

import com.sk89q.worldedit.regions.Region;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Parkour {
    public static Map<String, Region> maps = new HashMap<>();
    public static Map<Player, String> playingPlayers = new HashMap<>();
    public static Map<Player, Location> spawnPoint = new HashMap<>();
    public static Map<Player, Color> playersWithColor = new HashMap<>();
    public static Map<Player, GameMode> playerWithGameMode = new HashMap<>();

    public static Map<String, Shulker> shulkers = new HashMap<>();

    public static Map<Color, String> colorNames = new HashMap<>() {{
        put(ParkourColor.RED, "RED");
        put(ParkourColor.ORANGE, "ORANGE");
        put(ParkourColor.YELLOW, "YELLOW");
        put(ParkourColor.LIME, "LIME");
        put(ParkourColor.GREEN, "GREEN");
        put(ParkourColor.CYAN, "CYAN");
        put(ParkourColor.LIGHT_BLUE, "LIGHT_BLUE");
        put(ParkourColor.BLUE, "BLUE");
        put(ParkourColor.PURPLE, "PURPLE");
        put(ParkourColor.MAGENTA, "MAGENTA");
        put(ParkourColor.PINK, "PINK");
        put(ParkourColor.BROWN, "BROWN");
        put(ParkourColor.LIGHT_GRAY, "LIGHT_GRAY");
        put(ParkourColor.GRAY, "GRAY");
        put(ParkourColor.BLACK, "BLACK");
    }};

    public static void loadBlocks(Region region, World world) {
        Map<String, Location> blocks = new HashMap<>();

        region.forEach((vec) -> {
            Location loc = new Location(world, vec.getX(), vec.getY(), vec.getZ());

            if (world.getBlockAt(loc).getType() == Material.EMERALD_BLOCK) {
                //pass
            }
        });
    }
}
