package net.stockholm.itemsglowing.util;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NMS {
    public static ServerPlayer applyNmsPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
    public static net.minecraft.world.entity.Entity applyNmsEntity(Entity entity) {
        return ((CraftEntity) entity).getHandle();
    }
}