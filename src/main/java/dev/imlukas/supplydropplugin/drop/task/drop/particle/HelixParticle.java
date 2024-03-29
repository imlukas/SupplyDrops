package dev.imlukas.supplydropplugin.drop.task.drop.particle;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.locations.DropLocation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class HelixParticle {

    private final SupplyDropPlugin plugin;
    private final Location dropLocation;
    private final Particle particle;
    private final double initialHeight;
    private final double maxHeight;
    private final double radius;
    private final double angle;
    private final int delay;
    private final double increment;
    private final Task task;

    /**
     * @param particle      particle to spawn
     * @param initialHeight initial height of the particle
     * @param maxHeight     maximum height of the particle
     * @param radius        radius around the player's location to place particles around
     * @param angle         angle of the helix, the higher the more spins there are
     * @param increment     the height each iteration is increased by
     * @param delay         delay between particle spawns
     * @param time          maximum time of the particle
     */
    @SuppressWarnings("java:S107")
    public HelixParticle(SupplyDropPlugin plugin, Location location, Particle particle, double initialHeight, double maxHeight,
                         double radius, double angle, double increment, int delay) {
        this.plugin = plugin;
        this.dropLocation = location;
        this.particle = particle;
        this.initialHeight = initialHeight;
        this.maxHeight = maxHeight;
        this.radius = radius;
        this.angle = angle;
        this.delay = delay;
        this.increment = increment;

        this.task = new Task();
    }


    public void start() {
        task.runTaskTimerAsynchronously(plugin, 0, delay);
    }

    public void stop() {
        task.cancel();
    }

    private class Task extends BukkitRunnable {

        private final Location location = dropLocation;
        private final World world = location.getWorld();
        private double y = initialHeight;

        @Override
        public void run() {
            double particleAngle = y * angle;

            double newY = location.getY() + y;

            double x = location.getX() + (radius * Math.cos(particleAngle));
            double z = location.getZ() + (radius * Math.sin(particleAngle));
            double x2 = location.getX() - (radius * Math.cos(particleAngle));
            double z2 = location.getZ() - (radius * Math.sin(particleAngle));
            world.spawnParticle(particle, x, newY, z, 1, 0, 0, 0, 0);
            world.spawnParticle(particle, x2, newY, z2, 1, 0, 0, 0, 0);

            y += increment;
            if (y > maxHeight) {
                y = initialHeight;
            }
        }

    }

}
