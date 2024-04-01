package dev.imlukas.supplydropplugin.drop.messager;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.locations.DropLocation;
import dev.imlukas.supplydropplugin.util.text.NumberUtil;
import dev.imlukas.supplydropplugin.util.text.Placeholder;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DropMessager {


    public static void announceDropping(SupplyDropPlugin plugin, Drop drop, DropLocation location) {
        plugin.getMessages().announce("drop.dropping", getPlaceholders(drop, location));
    }

    public static void sendCollectNotifications(SupplyDropPlugin plugin, Player player, Drop drop) {
        Placeholder<Audience> type = new Placeholder<>("drop-type", drop.getTypeId());
        plugin.getMessages().send(player, "drop.collected", type);
        plugin.getSounds().playSound(player, "drop.collected");
    }

    public static void announceCollected(SupplyDropPlugin plugin, Player player, Drop drop, DropLocation location) {
        List<Placeholder<Audience>> placeholders = getPlaceholders(drop, location);
        placeholders.add(new Placeholder<>("player", player.getDisplayName()));

        plugin.getMessages().announce("drop.collected-announce", placeholders);
    }

    private static List<Placeholder<Audience>> getPlaceholders(Drop drop, DropLocation location) {
        List<Placeholder<Audience>> placeholders = new ArrayList<>();
        Placeholder<Audience> x = new Placeholder<>("location-x", NumberUtil.formatDouble(location.getX(), 1));
        Placeholder<Audience> y = new Placeholder<>("location-y", NumberUtil.formatDouble(location.getY(), 1));
        Placeholder<Audience> z = new Placeholder<>("location-z", NumberUtil.formatDouble(location.getZ(), 1));
        Placeholder<Audience> type = new Placeholder<>("drop-type", drop.getTypeId());
        placeholders.add(x);
        placeholders.add(y);
        placeholders.add(z);
        placeholders.add(type);
        return placeholders;
    }
}
