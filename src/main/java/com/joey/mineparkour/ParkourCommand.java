package com.joey.mineparkour;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;
import dev.jorel.commandapi.CommandAPI;
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
                        .executesPlayer((player, args) -> {
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
                        })
                )
                .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(new StringArgument("name").overrideSuggestions(maps.keySet()))
                        .executesPlayer((player, args) -> {
                            String name = (String) args[0];

                            if (maps.containsKey(name)) {
                                maps.remove(name);
                                player.sendMessage("map [" + name + "] is removed");
                            }
                            else {
                                CommandAPI.fail("map [" + name + "] doesn't exist");
                            }
                        })
                )
                .withSubcommand(new CommandAPICommand("start")
                        .withArguments(new StringArgument("name").overrideSuggestions(maps.keySet()))
                        .executesPlayer((player, args) -> {
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

                                    for (Color i : PlayerColor.getValues()) {
                                        if (!playersWithColor.containsValue(i)) {
                                            playersWithColor.put(player, PlayerColor.LIME);
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
                        })
                )
                .withSubcommand(new CommandAPICommand("quit")
                        .executesPlayer((player, args) -> {
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
                                CommandAPI.fail("player [" + player.getName() + "] isn't playing game");
                            }
                        })
                )
                .register();

        new CommandAPICommand("parkour")
                .withAliases("pk")
                .withPermission(CommandPermission.OP)
                .withSubcommand(new CommandAPICommand("start")
                        .withArguments(new StringArgument("name").overrideSuggestions(maps.keySet()))
                        .withArguments(new PlayerArgument("player"))
                        .executesPlayer((player, args) -> {
                            Player target = (Player) args[1];

                            if (playingPlayers.containsKey(target)) {
                                target.sendMessage("You are already playing games");
                                return;
                            }

                            String name = (String) args[0];
                            Region region = maps.get(name);
                            region.forEach((vec) -> {
                                Location loc = new Location(target.getWorld(), vec.getX(), vec.getY(), vec.getZ());

                                if (target.getWorld().getBlockAt(loc).getType() == Material.EMERALD_BLOCK) {
                                    target.teleport(loc.add(0.5, 1, 0.5));

                                    playingPlayers.put(target, name);

                                    for (Color i : PlayerColor.getValues()) {
                                        if (!playersWithColor.containsValue(i)) {
                                            playersWithColor.put(target, PlayerColor.LIME);
                                            break;
                                        }
                                    }
                                }
                            });
                        })
                )
                .withSubcommand(new CommandAPICommand("quit")
                        .withArguments(new PlayerArgument("player"))
                        .executesPlayer((player, args) -> {
                            Player target = (Player) args[0];

                            if (playingPlayers.containsKey(target)) {
                                target.sendMessage("player [" + target.getName() + "] quits map [" + playingPlayers.get(target) + "]");

                                target.setGameMode(GameMode.CREATIVE);

                                playingPlayers.remove(target);
                            }
                            else {
                                CommandAPI.fail("player [" + target.getName() + "] isn't playing game");
                            }
                        })
                )
                .register();
    }

}
