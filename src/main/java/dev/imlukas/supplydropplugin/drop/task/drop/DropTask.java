package dev.imlukas.supplydropplugin.drop.task.drop;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.cache.DropCache;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.util.file.PluginSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DropTask implements Runnable {

    private final DropCache cache;
    private final DropSupplier dropSupplier;

    public DropTask(SupplyDropPlugin plugin) {
        this.dropSupplier = plugin.getDropSupplier();
        this.cache = plugin.getDropCache();

        PluginSettings settings = plugin.getSettings();
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0, settings.getTimePerDrop());
    }

    @Override
    public void run() {
        Drop drop = dropSupplier.supplyDrop();

        Location location = drop.getLocation().asBukkitLocation();

        for (Drop cacheDrop : cache.getCached().values()) { // Check if the drop is too close to another drop
            Location cacheLocation = cacheDrop.getLocation().asBukkitLocation();

            if (location.distance(cacheLocation) < 10) {
                return;
            }
        }

        drop.drop();
        cache.add(drop);
    }
}
