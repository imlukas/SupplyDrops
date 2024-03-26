package dev.imlukas.supplydropplugin.commands;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.cache.DropCache;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.util.core.audience.bukkit.BukkitPlayerCommandAudience;
import dev.imlukas.supplydropplugin.util.core.context.arg.StringArgument;

import java.util.ArrayList;
import java.util.Queue;

public class SupplyDropCommand {


    public SupplyDropCommand(SupplyDropPlugin plugin) {
        DropCache cache = plugin.getDropCache();
        DropSupplier supplier = plugin.getDropSupplier();

        plugin.getCommandManager()
                .newCommand("supplydrops")
                .registerArgument("drop")
                .registerArgument(StringArgument.create("drop-id").tabComplete(new ArrayList<>(supplier.getConfiguration().getKeys(false))))
                .audience(BukkitPlayerCommandAudience.class)
                .handler((sender, args) -> {
                    String dropId = args.getArgument("drop-id");
                    Drop drop = supplier.supplyDrop(dropId);

                    boolean dropped = drop.drop();

                    if (!dropped) {
                        return;
                    }

                    cache.add(drop);
                }).build();
    }
}
