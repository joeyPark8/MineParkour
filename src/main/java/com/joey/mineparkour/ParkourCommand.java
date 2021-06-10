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
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.joey.mineparkour.Parkour.*;

public class ParkourCommand {
    public void register() {
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

                                    playersWithColor.put(player, Material.LIME_BANNER);

                                    String color = this.deleteBanner(playersWithColor.get(player).toString().toCharArray());

                                    player.sendMessage(player.getName() + ": " + color);
                                }
                            });
                        })
                )
                .withSubcommand(new CommandAPICommand("quit")
                        .executesPlayer((player, args) -> {
                            if (playingPlayers.containsKey(player)) {
                                player.sendMessage("player [" + player.getName() + "] quits map [" + playingPlayers.get(player) + "]");

                                playingPlayers.remove(player);
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

                                playingPlayers.remove(target);
                            }
                            else {
                                CommandAPI.fail("player [" + target.getName() + "] isn't playing game");
                            }
                        })
                )
                .register();
    }

    public String deleteBanner(char[] chars) {
        String str = "";
        int length = chars.length - 7;

        for (int i = 0; i < length; i+=1) str += chars[i];

        return str;
    }

}
