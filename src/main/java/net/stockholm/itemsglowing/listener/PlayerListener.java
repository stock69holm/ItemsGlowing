package net.stockholm.itemsglowing.listener;

import net.stockholm.itemsglowing.glowingitem.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerListener implements Listener {
    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        Item.getItemManager(event.getPlayer()).findItems();
    }
    @EventHandler
    private void onItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }
    @EventHandler
    private void onSwap(PlayerSwapHandItemsEvent event) {
        if (Item.getItemManager(event.getPlayer()).pickup()) event.setCancelled(true);
    }
}