package dev.imlukas.supplydropplugin.drop;

import dev.imlukas.supplydropplugin.location.SafeLocation;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Drop {

    UUID getId();

    SafeLocation getLocation();

    void drop();

    void destroy();

    void collect(Player player);
}
