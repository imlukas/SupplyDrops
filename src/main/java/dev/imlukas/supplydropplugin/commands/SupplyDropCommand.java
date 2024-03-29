package dev.imlukas.supplydropplugin.commands;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.locations.DropLocation;
import dev.imlukas.supplydropplugin.drop.tracker.DropTracker;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.util.core.audience.bukkit.BukkitPlayerCommandAudience;
import dev.imlukas.supplydropplugin.util.core.context.arg.BooleanArgument;
import dev.imlukas.supplydropplugin.util.core.context.arg.StringArgument;
import dev.imlukas.supplydropplugin.util.file.PluginSettings;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SupplyDropCommand {


    public SupplyDropCommand(SupplyDropPlugin plugin) {
        DropTracker tracker = plugin.getDropTracker();
        PluginSettings settings = plugin.getSettings();
        DropSupplier supplier = plugin.getDropSupplier();

        plugin.getCommandManager()
                .newCommand("supplydrops")
                .registerArgument("drop")
                .registerArgument(BooleanArgument.create("player-location"))
                .registerArgument(StringArgument.create("drop-id").tabComplete(new ArrayList<>(supplier.getConfiguration().getKeys(false))))
                .audience(BukkitPlayerCommandAudience.class)
                .handler((sender, args) -> {
                    String dropId = args.getArgument("drop-id");
                    boolean playerLocation = args.getArgument("player-location");

                    Player player = sender.getPlayer();
                    Drop drop = supplier.supplyDrop(dropId);

                    if (playerLocation) {
                        Location location = player.getLocation();
                        location.setY(settings.getPlayerLocationHeight());
                        drop.dropAt(DropLocation.fromLocation(location));
                        tracker.add(drop);
                        return;
                    }

                    boolean dropped = drop.drop();

                    if (!dropped) {
                        return;
                    }

                    tracker.add(drop);
                }).build();
    }
}
