package net.stockholm.itemsglowing.util;

import org.bukkit.ChatColor;

public class Color {
    public static String apply(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
