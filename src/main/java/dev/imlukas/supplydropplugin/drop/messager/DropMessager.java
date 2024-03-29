package dev.imlukas.supplydropplugin.drop.messager;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.locations.DropLocation;
import dev.imlukas.supplydropplugin.util.text.Placeholder;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

public class DropMessager {


    public static void announceDropping(SupplyDropPlugin plugin, Drop drop, DropLocation location) {

        Placeholder<Audience> type = new Placeholder<>("drop-type", drop.getTypeId());
        Placeholder<Audience> x = new Placeholder<>("location-x", String.valueOf(location.getX()));
        Placeholder<Audience> y = new Placeholder<>("location-y", String.valueOf(location.getY()));
        Placeholder<Audience> z = new Placeholder<>("location-z", String.valueOf(location.getZ()));
        plugin.getMessages().announce("drop.dropping", type, x, y, z);
    }

    public static void sendCollectNotifications(SupplyDropPlugin plugin, Player player, Drop drop) {
        Placeholder<Audience> type = new Placeholder<>("drop-type", drop.getTypeId());
        plugin.getMessages().send(player, "drop.collected", type);
        plugin.getSounds().playSound(player, "drop.collected");
    }

    public static void announceCollected(SupplyDropPlugin plugin, Player player, Drop drop, DropLocation location) {
        Placeholder<Audience> type = new Placeholder<>("drop-type", drop.getTypeId());
        Placeholder<Audience> playerPlaceholder = new Placeholder<>("player", player.getDisplayName());
        Placeholder<Audience> x = new Placeholder<>("location-x", String.valueOf(location.getX()));
        Placeholder<Audience> y = new Placeholder<>("location-y", String.valueOf(location.getY()));
        Placeholder<Audience> z = new Placeholder<>("location-z", String.valueOf(location.getZ()));
        plugin.getMessages().announce("drop.collected-announce", type, playerPlaceholder, x, y, z);
    }
}
