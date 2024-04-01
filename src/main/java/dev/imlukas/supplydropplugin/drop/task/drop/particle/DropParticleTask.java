package dev.imlukas.supplydropplugin.drop.task.drop.particle;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.util.file.PluginSettings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

public class DropParticleTask {

    private final CircleParticle circleParticle;

    public DropParticleTask(SupplyDropPlugin plugin, String particleType, ArmorStand stand) {
        PluginSettings settings = plugin.getSettings();

        Location location = stand.getLocation();
        World world = location.getWorld();
        Location groundLocation = world.getHighestBlockAt(location).getLocation();
        groundLocation.add(0, 1, 0);

        if (particleType.equalsIgnoreCase("circle")) {
            circleParticle = new CircleParticle(plugin, groundLocation, settings.getCircleRadius(), settings.getCircleDelay());
        } else {
            circleParticle = new RainbowCircleParticle(plugin, groundLocation, settings.getCircleRadius(), settings.getCircleDelay());
        }

        circleParticle.start();
    }

    public void stop() {
        if (circleParticle != null) {
            circleParticle.stop();
        }
    }
}
