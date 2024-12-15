package net.stockholm.itemsglowing.util;

import net.stockholm.itemsglowing.Main;
import net.stockholm.itemsglowing.config.Config;

public class Settings {
    public static String PICKUP_MESSAGE;
    public static String PICKUP_SOUND;
    public static int PICKUP_DISTANCE;
    static {
        Config config = Main.getInstance().getPluginConfig();

        PICKUP_MESSAGE = config.getString("pickupMessage");
        PICKUP_SOUND = config.getString("pickupSound");
        PICKUP_DISTANCE = config.getInt("pickupDistance");
    }
}