package com.joey.mineparkour;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

import static com.joey.mineparkour.Parkour.maps;

public class ParkourCommand {
    public static void register() {
        new CommandAPICommand("parkour")
                .withAliases("pk")
                .withPermission(CommandPermission.OP)
                .withSubcommand(new CommandAPICommand("create")
                        .withArguments(new StringArgument("name"))
                        .executesPlayer((player, args) -> {
                            String name = (String) args[0];

                            Player actor = BukkitAdapter.adapt(player);
                            SessionManager manager = WorldEdit.getInstance().getSessionManager();
                            LocalSession localSession = manager.get(actor);
                            Region region;

                            World selectionWorld = localSession.getSelectionWorld();
                            try {
                                if (selectionWorld == null) throw new IncompleteRegionException();
                                region = localSession.getSelection(selectionWorld);

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
                                player.sendMessage("region [" + name + "] is removed");
                            }
                            else CommandAPI.fail("region [" + name + "] doesn't exist");
                        })
                )
                .withSubcommand(new CommandAPICommand("start")
                        .withArguments(new StringArgument("name").overrideSuggestions(maps.keySet()))
                        .withArguments(new PlayerArgument("player"))
                        .executesPlayer((player, args) -> {
                            //blablabla
                        })
                )
                .withSubcommand(new CommandAPICommand("quit")
                        .executesPlayer((player, args) -> {
                            //blablabla
                        })
                )
                .register();
    }

}
