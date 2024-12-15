package net.stockholm.itemsglowing.config;

import net.stockholm.itemsglowing.util.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigImpl implements Config {
    FileConfiguration fileConfiguration;

    public ConfigImpl(JavaPlugin plugin) {
        this.fileConfiguration = plugin.getConfig();
    }

    @Override
    public String getString(String path) {
        return Color.apply(fileConfiguration.getString(path));
    }

    @Override
    public int getInt(String path) {
        return fileConfiguration.getInt(path);
    }
}