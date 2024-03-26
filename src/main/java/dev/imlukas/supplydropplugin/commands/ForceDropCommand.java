package dev.imlukas.supplydropplugin.commands;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.cache.DropCache;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.util.core.audience.bukkit.BukkitPlayerCommandAudience;

public class ForceDropCommand {


    public ForceDropCommand(SupplyDropPlugin plugin) {
        DropCache cache = plugin.getDropCache();
        DropSupplier supplier = plugin.getDropSupplier();

        plugin.getCommandManager().newCommand("forcedrop").audience(BukkitPlayerCommandAudience.class).handler((sender, args) -> {
            Drop drop = supplier.supplyDrop();
            cache.add(drop);
            drop.drop();
        }).build();
    }
}
