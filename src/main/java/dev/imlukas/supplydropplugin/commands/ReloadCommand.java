package dev.imlukas.supplydropplugin.commands;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.drop.tracker.DropTracker;
import dev.imlukas.supplydropplugin.util.core.audience.bukkit.BukkitPlayerCommandAudience;
import dev.imlukas.supplydropplugin.util.core.context.arg.StringArgument;

import java.util.ArrayList;

public class ReloadCommand  {

    public ReloadCommand(SupplyDropPlugin plugin) {

        plugin.getCommandManager()
                .newCommand("supplydrops")
                .registerArgument("reload")
                .permission("supplydrops.admin")
                .audience(BukkitPlayerCommandAudience.class)
                .handler((sender, args) -> {
                    plugin.reload();
                    sender.sendMessage("SupplyDrops reloaded!");
                }).build();
    }
}
