package com.joey.mineparkour;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static com.joey.mineparkour.Parkour.maps;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println(this.getName() + " is activated");

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

                            int width = region.getWidth();
                            int height = region.getHeight();

                            player.sendMessage("name: " + name + ", width: " + width + ", height: " + height);
                        } catch (IncompleteRegionException e) {
                            actor.printError(TextComponent.of("Please make a region selection first."));
                        }
                    })
                )
                .withSubcommand(new CommandAPICommand("remove")
                    .withArguments(new StringArgument("name").overrideSuggestions(maps.keySet()))
                    .executesPlayer((player, args) -> {
                        //blablabla
                    })
                )
                .withSubcommand(new CommandAPICommand("start")
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

    @Override
    public void onDisable() {
        System.out.println(this.getName() + " is deactivated");
    }

    public WorldEditPlugin getWordEdit() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

        if (plugin instanceof WorldEditPlugin) return (WorldEditPlugin) plugin;
        return null;
    }
}
