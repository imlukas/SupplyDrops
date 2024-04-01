package dev.imlukas.supplydropplugin.drop.task.drop;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.task.drop.particle.DropParticleTask;
import dev.imlukas.supplydropplugin.util.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicInteger;

public class DropFallTask {

    public DropFallTask(SupplyDropPlugin plugin, String displayName, String particleType, ArmorStand armorStand) {

        DropParticleTask particleTask = new DropParticleTask(plugin, particleType, armorStand);

        Location location = armorStand.getLocation();
        World world = location.getWorld();
        AtomicInteger highestBlockY = new AtomicInteger(world.getHighestBlockYAt(location));
        Bukkit.getScheduler().runTaskTimer(plugin, (runnable -> {
            Location currentLocation = armorStand.getLocation();
            int distanceToGround = currentLocation.getBlockY() - highestBlockY.get() - 1;

            if (distanceToGround < 0) { // Update distance if block was broken
                highestBlockY.set(world.getHighestBlockYAt(currentLocation));
                return;
            }

            if (!displayName.isEmpty()) {
                armorStand.customName(TextUtils.color(displayName.replace("%distance%", String.valueOf(distanceToGround))));
            }

            armorStand.setVelocity(new Vector(0, plugin.getSettings().getDropVelocity(), 0));

            if (distanceToGround == 0) {
                runnable.cancel();
                particleTask.stop();
                return;
            }

            if (armorStand.isDead()) {
                particleTask.stop();
                runnable.cancel();
            }
        }), 0, 2);
    }
}
