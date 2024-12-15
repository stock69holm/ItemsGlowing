package net.stockholm.itemsglowing.sound;

import org.bukkit.entity.Player;

public class SoundManagerImpl implements SoundManager {
    Player player;

    public SoundManagerImpl(Player player) {
        this.player = player;
    }

    @Override
    public void activateSound(String sound) {
        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
    }
}
