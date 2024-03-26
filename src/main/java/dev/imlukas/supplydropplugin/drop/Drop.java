package dev.imlukas.supplydropplugin.drop;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Drop {

    UUID getEntityId();

    UUID getUUID();

    boolean drop();

    void destroy();

    void collect(Player player);
}
