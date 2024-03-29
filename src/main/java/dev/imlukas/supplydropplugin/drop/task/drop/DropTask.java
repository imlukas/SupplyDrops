package dev.imlukas.supplydropplugin.drop.task.drop;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.tracker.DropTracker;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.util.file.PluginSettings;
import dev.imlukas.supplydropplugin.util.file.messages.Messages;
import dev.imlukas.supplydropplugin.util.text.Placeholder;
import dev.imlukas.supplydropplugin.util.time.Time;
import dev.imlukas.supplydropplugin.util.time.TimeUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;

import java.util.Queue;

public class DropTask implements Runnable {

    private final Messages messages;
    private final PluginSettings settings;
    private final DropTracker dropTracker;
    private final DropSupplier dropSupplier;
    private long currentTicks = 0;

    public DropTask(SupplyDropPlugin plugin) {
        this.messages = plugin.getMessages();
        this.settings = plugin.getSettings();
        this.dropTracker = plugin.getDropTracker();
        this.dropSupplier = plugin.getDropSupplier();

        Bukkit.getScheduler().runTaskTimer(plugin, this, 20, 20);
    }

    public long getTicksRemaining() {
        return settings.getTimePerDrop() - currentTicks;
    }

    @Override
    public void run() {
        currentTicks += 20;

        if (settings.getNotificationTicks().contains(currentTicks)) {
            long seconds = getTicksRemaining() / 20;
            Placeholder<Audience> hours = new Placeholder<>("hours_remaining", String.valueOf(seconds / 3600));
            Placeholder<Audience> minutes = new Placeholder<>("minutes_remaining", String.valueOf((seconds % 3600) / 60));
            Placeholder<Audience> secondsPlaceholder = new Placeholder<>("seconds_remaining", String.valueOf(seconds % 60));

            messages.announce("drop.notification", hours, minutes, secondsPlaceholder);
        }

        if (currentTicks < settings.getTimePerDrop()) {
            return;
        }


        currentTicks = 0;
        Drop drop = dropSupplier.supplyDrop(settings.getDefaultDropId());

        if (drop == null) {
            System.out.println("Couldn't find drop for id " + settings.getDefaultDropId());
            return;
        }

        boolean dropped = drop.drop();

        if (!dropped) {
            return;
        }

        dropTracker.add(drop);
    }
}
