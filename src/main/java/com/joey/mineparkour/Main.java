package com.joey.mineparkour;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static com.joey.mineparkour.Parkour.shulkers;
import static com.joey.mineparkour.ParkourCommand.register;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println(this.getName() + " is activated");

        getServer().getPluginManager().registerEvents(new ParkourEvents(), this);

        register();
    }

    @Override
    public void onDisable() {
        System.out.println(this.getName() + " is deactivated");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("down")) {
            Shulker shulker = shulkers.get(args[0]);
            float speed = Float.parseFloat(args[1]) / 2;

            final FallingBlock[] lastBlock = {null};

            if (shulker != null) {
                shulker.setGlowing(false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        shulker.teleport(shulker.getLocation().add(0, -speed, 0));

                        if (lastBlock[0] != null) {
                            lastBlock[0].remove();
                        }

                        BlockData data = Material.SAND.createBlockData();
                        FallingBlock fallingBlock = player.getWorld().spawnFallingBlock(shulker.getLocation(), data);

                        lastBlock[0] = fallingBlock;

                        if (shulker.getLocation().getY() < 0) {
                            shulker.remove();
                            lastBlock[0].remove();
                            cancel();
                        }
                    }
                }.runTaskTimer(this, 0, 1);
            }
        }

        return false;
    }
}
