package dev.imlukas.supplydropplugin.util.core.audience.bukkit;

import org.bukkit.entity.Player;

public class BukkitPlayerCommandAudience extends BukkitCommandSenderCommandAudience {

    private final Player player;

    public BukkitPlayerCommandAudience(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public boolean isConsole() {
        return false;
    }

    public Player getPlayer() {
        return player;
    }
}
