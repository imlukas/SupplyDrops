package dev.imlukas.supplydropplugin.drop.task.drop.particle;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.util.file.PluginSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

public class DropParticleTask {

    private final HelixParticle helixParticle;
    public DropParticleTask(SupplyDropPlugin plugin, ArmorStand stand) {
        PluginSettings settings = plugin.getSettings();

        Location location = stand.getLocation();
        World world = location.getWorld();
        Location groundLocation = world.getHighestBlockAt(location).getLocation();

        helixParticle = new HelixParticle(plugin, groundLocation,
                settings.getHelixParticle(),
                settings.getHelixInitialHeight(),
                settings.getHelixMaxHeight(),
                settings.getHelixRadius(),
                settings.getHelixAngle(),
                settings.getHelixIncrement(),
                settings.getHelixDelay());

        helixParticle.start();
    }

    public void stop() {
        if (helixParticle != null) {
            helixParticle.stop();
        }
    }
}
