package net.stockholm.itemsglowing.glowingitem;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public interface ItemManager {
    void findItems();
    void applyGlow(Item item);
    void removeGlow(Item item);
    boolean isLocationSane(org.bukkit.entity.Item item, int radius);
    boolean pickup();
    boolean addToEmptySlot(ItemStack itemStack);
    boolean unite(ItemStack itemStack);
}