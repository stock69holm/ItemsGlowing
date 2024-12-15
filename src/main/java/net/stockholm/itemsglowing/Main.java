package net.stockholm.itemsglowing;

import net.stockholm.itemsglowing.config.Config;
import net.stockholm.itemsglowing.config.ConfigImpl;
import net.stockholm.itemsglowing.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Main extends JavaPlugin {
    private static Main instance;
    private Config config;
    @Override
    public void onEnable() {
        instance = this;
        getDataFolder().mkdirs();
        saveDefaultConfig();
        config = new ConfigImpl(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getLogger().log(Level.INFO, "init..");
    }

    @Override
    public void onDisable() {
    }

    public Config getPluginConfig() {
        return config;
    }

    public static Main getInstance() {
        return instance;
    }
}
