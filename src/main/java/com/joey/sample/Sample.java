package com.joey.sample;

import org.bukkit.plugin.java.JavaPlugin;

public class Sample extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println(this.getName() + " is activated");
    }

    @Override
    public void onDisable() {
        System.out.println(this.getName() + " is deactivated");
    }
}
