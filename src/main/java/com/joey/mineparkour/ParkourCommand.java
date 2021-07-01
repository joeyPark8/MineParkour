package com.joey.mineparkour;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;

import java.awt.*;

import static com.joey.mineparkour.Parkour.*;

public class ParkourCommand {
    public static void register() {
        new CommandAPICommand("parkour")
                .withAliases("pk")
                .withPermission(CommandPermission.OP)
                .withSubcommand(new CommandAPICommand("create")
                        .withArguments(new StringArgument("name"))
                        .executesPlayer(ParkourCommand::create)
                )
                .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(new StringArgument("name").overrideSuggestions(maps.keySet()))
                        .executesPlayer(ParkourCommand::remove)
                )
                .withSubcommand(new CommandAPICommand("start")
                        .withArguments(new StringArgument("name").overrideSuggestions(maps.keySet()))
                        .executesPlayer(ParkourCommand::start)
                )
                .withSubcommand(new CommandAPICommand("quit")
                        .executesPlayer(ParkourCommand::quit)
                )
                .register();

        new CommandAPICommand("parkour")
                .withAliases("pk")
                .withPermission(CommandPermission.OP)
                .withSubcommand(new CommandAPICommand("start")
                        .withArguments(new StringArgument("name").overrideSuggestions(maps.keySet()))
                        .withArguments(new PlayerArgument("player"))
                        .executesPlayer(ParkourCommand::start)
                )
                .withSubcommand(new CommandAPICommand("quit")
                        .withArguments(new PlayerArgument("player"))
                        .executesPlayer(ParkourCommand::quit)
                )
                .register();
    }

    public static void create(Player player, Object[] args) {
        String name = (String) args[0];

        com.sk89q.worldedit.entity.Player actor = BukkitAdapter.adapt(player);
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        LocalSession localSession = manager.get(actor);
        Region region;

        World selectionWorld = localSession.getSelectionWorld();
        try {
            if (selectionWorld == null) throw new IncompleteRegionException();
            region = localSession.getSelection(selectionWorld);

            player.sendMessage("map [" + name + "] is created");

            maps.put(name, region);
        } catch (IncompleteRegionException e) {
            actor.printError(TextComponent.of("Please make a region selection first."));
        }
    }

    public static void remove(Player player, Object[] args) {
        String name = (String) args[0];

        if (maps.containsKey(name)) {
            maps.remove(name);
            player.sendMessage("map [" + name + "] is removed");
        }
        else {
            player.sendMessage(ChatColor.RED + "map [" + name + "] doesn't exist");
        }
    }

    public static void start(Player player, Object[] args) {
        if (playingPlayers.containsKey(player)) {
            player.sendMessage("You are already playing games");
            return;
        }

        String name = (String) args[0];
        Region region = maps.get(name);
        region.forEach((vec) -> {
            Location loc = new Location(player.getWorld(), vec.getX(), vec.getY(), vec.getZ());

            if (player.getWorld().getBlockAt(loc).getType() == Material.EMERALD_BLOCK) {
                player.teleport(loc.add(0.5, 1, 0.5));
                player.setGameMode(GameMode.SURVIVAL);
                playingPlayers.put(player, name);
                spawnPoint.put(player, loc);
                playerWithGameMode.put(player, player.getGameMode());

                for (Color i : ParkourColor.getValues()) {
                    if (!playersWithColor.containsValue(i)) {
                        playersWithColor.put(player, ParkourColor.LIME);
                        break;
                    }
                }

                String colorName = colorNames.get(playersWithColor.get(player));

                player.sendMessage(player.getName() + ": " + ChatColor.of(playersWithColor.get(player)) + colorName);

                Shulker shulker = (Shulker) player.getWorld().spawnEntity(player.getLocation().add(0, 3, 0), EntityType.SHULKER);
                shulker.setAI(false);
                shulker.setAware(false);
                shulker.setGravity(false);
                shulker.setCollidable(true);
                shulker.setInvisible(true);
                shulker.setGlowing(true);
                shulker.setInvulnerable(true);
                shulker.setRotation(0, 45);

                shulkers.put(name, shulker);
            }
        });
    }

    public static void quit(Player player, Object[] args) {
        if (playingPlayers.containsKey(player)) {
            player.sendMessage("player [" + player.getName() + "] quits map [" + playingPlayers.get(player) + "]");

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
        else {
            player.sendMessage(ChatColor.RED + "player [" + player.getName() + "] isn't playing game");
        }
    }

}
