package com.joey.mineparkour;

import org.bukkit.plugin.java.JavaPlugin;

import static com.joey.mineparkour.ParkourCommand.register;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println(this.getName() + " is activated");

        register();
    }

    @Override
    public void onDisable() {
        System.out.println(this.getName() + " is deactivated");
    }

}
