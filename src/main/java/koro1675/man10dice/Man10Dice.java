package koro1675.man10dice;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Man10Dice extends JavaPlugin implements Listener {

    Man10DiceManager man10DiceManager = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("mdice").setExecutor(new Man10DiceCommands(this));
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        man10DiceManager = new Man10DiceManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
