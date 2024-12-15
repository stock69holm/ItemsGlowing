package net.stockholm.itemsglowing.glowingitem;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.stockholm.itemsglowing.sound.SoundManager;
import net.stockholm.itemsglowing.sound.SoundManagerImpl;
import net.stockholm.itemsglowing.util.NMS;
import net.stockholm.itemsglowing.util.Settings;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Objects;

public class ItemManagerImpl implements ItemManager {
    Player player;
    PlayerInventory inventory;
    ServerPlayer serverPlayer;
    SoundManager soundManager;

    public ItemManagerImpl(Player viewer) {
        this.player = viewer;
        this.inventory = player.getInventory();
        this.serverPlayer = NMS.applyNmsPlayer(viewer);
        this.soundManager = new SoundManagerImpl(player);
    }

    @Override
    public void findItems() {
        int radius = Settings.PICKUP_DISTANCE;
        org.bukkit.entity.Item glowingItem = ItemData.glowingItem.get(player);

        if (glowingItem == null && player.getNearbyEntities(radius, radius, radius).stream().noneMatch(e -> e instanceof org.bukkit.entity.Item)) {
            return;
        }

        if (glowingItem != null) {
            removeGlow(glowingItem);
        }

        player.getNearbyEntities(radius, radius, radius).stream()
                .filter(entity -> entity instanceof org.bukkit.entity.Item)
                .map(entity -> (org.bukkit.entity.Item) entity)
                .filter(item -> isLocationSane(item, radius))
                .findFirst()
                .ifPresent(this::applyGlow);
    }

    @Override
    public void applyGlow(org.bukkit.entity.Item item) {
        ItemEntity nmsItem = (ItemEntity) NMS.applyNmsEntity(item);
        SynchedEntityData data = nmsItem.getEntityData();
        EntityDataAccessor<Byte> flags = new EntityDataAccessor<>(0, EntityDataSerializers.BYTE);

        byte currentFlags = data.get(flags);
        data.set(flags, (byte) (currentFlags | 0x40));

        ServerPlayer nmsPlayer = NMS.applyNmsPlayer(player);
        Connection connection = nmsPlayer.connection.connection;
        connection.send(new ClientboundSetEntityDataPacket(nmsItem.getId(), Objects.requireNonNull(data.packDirty())));

        player.sendTitle("", Settings.PICKUP_MESSAGE, 0, 20, 0);
        ItemData.glowingItem.put(player, item);
    }

    @Override
    public void removeGlow(org.bukkit.entity.Item item) {
        ItemEntity oldNmsItem = (ItemEntity) NMS.applyNmsEntity(item);
        SynchedEntityData data = oldNmsItem.getEntityData();
        EntityDataAccessor<Byte> flags = new EntityDataAccessor<>(0, EntityDataSerializers.BYTE);

        byte currentFlags = data.get(flags);
        data.set(flags, (byte) (currentFlags & ~0x40));

        ServerPlayer nmsPlayer = NMS.applyNmsPlayer(player);
        Connection connection = nmsPlayer.connection.connection;
        connection.send(new ClientboundSetEntityDataPacket(oldNmsItem.getId(), Objects.requireNonNull(data.packDirty())));

        ItemData.glowingItem.put(player, null);
    }

    @Override
    public boolean isLocationSane(org.bukkit.entity.Item item, int radius) {
        Block targetBlock = player.getTargetBlockExact(radius);
        return targetBlock != null && item.getLocation().distanceSquared(targetBlock.getLocation()) <= Settings.PICKUP_DISTANCE;
    }

    @Override
    public boolean pickup() {
        org.bukkit.entity.Item glowingItem = ItemData.glowingItem.get(player);
        if (glowingItem == null) {
            return false;
        }

        ItemStack itemStack = glowingItem.getItemStack();

        if (addToEmptySlot(itemStack)) {
            glowingItem.remove();
            ItemData.glowingItem.put(player, null);
            return true;
        }

        if (unite(itemStack)) {
            glowingItem.remove();
            soundManager.activateSound(Settings.PICKUP_SOUND);
            ItemData.glowingItem.put(player, null);
            return true;
        }

        return false;
    }

    @Override
    public boolean addToEmptySlot(ItemStack itemStack) {
        int emptySlot = inventory.firstEmpty();
        if (emptySlot != -1) {
            inventory.addItem(itemStack);
            return true;
        }
        return false;
    }

    @Override
    public boolean unite(ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (i == inventory.getHeldItemSlot()) {
                continue;
            }

            ItemStack slotItem = inventory.getItem(i);
            if (slotItem != null && slotItem.isSimilar(itemStack) && slotItem.getAmount() < slotItem.getMaxStackSize()) {
                int freeSpace = slotItem.getMaxStackSize() - slotItem.getAmount();
                if (itemStack.getAmount() <= freeSpace) {
                    slotItem.setAmount(slotItem.getAmount() + itemStack.getAmount());
                } else {
                    itemStack.setAmount(itemStack.getAmount() - freeSpace);
                    slotItem.setAmount(slotItem.getMaxStackSize());
                }
                return true;
            }
        }
        return false;
    }
}