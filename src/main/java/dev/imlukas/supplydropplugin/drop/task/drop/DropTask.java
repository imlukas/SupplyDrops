package dev.imlukas.supplydropplugin.drop.task.drop;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.cache.DropCache;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import org.bukkit.scheduler.BukkitRunnable;

public class DropTask extends BukkitRunnable {

    private final SupplyDropPlugin plugin;
    private final DropCache cache;
    private final DropSupplier dropSupplier;

    public DropTask(SupplyDropPlugin plugin) {
        this.plugin = plugin;
        this.dropSupplier = plugin.getDropSupplier();
        this.cache = plugin.getDropCache();
    }

    @Override
    public void run() {
        // TODO: Implement drop task
    }
}
