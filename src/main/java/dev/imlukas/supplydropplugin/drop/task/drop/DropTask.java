package dev.imlukas.supplydropplugin.drop.task.drop;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.cache.DropCache;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.util.file.PluginSettings;
import org.bukkit.Bukkit;

import java.util.Queue;

public class DropTask implements Runnable {

    private final PluginSettings settings;
    private final DropCache cache;
    private final DropSupplier dropSupplier;
    private final Queue<Drop> queue;

    public DropTask(SupplyDropPlugin plugin) {
        this.settings = plugin.getSettings();
        this.cache = plugin.getDropCache();
        this.dropSupplier = plugin.getDropSupplier();
        this.queue = plugin.getDropQueue();

        System.out.println("Dropping a new drop every " + (settings.getTimePerDrop() / 20) / 60 + "minutes");
        Bukkit.getScheduler().runTaskTimer(plugin, this, settings.getTimePerDrop(), settings.getTimePerDrop());
    }

    @Override
    public void run() {
        Drop drop = dropSupplier.supplyDrop(settings.getDefaultDropId());

        if (drop == null) {
            System.out.println("Couldn't find drop for id " + settings.getDefaultDropId());
            return;
        }

        drop(drop);
    }

    public void drop(Drop drop) {
        boolean dropped = drop.drop();

        if (!dropped) {
            return;
        }

        cache.add(drop);
    }
}
