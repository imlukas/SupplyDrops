package dev.imlukas.supplydropplugin.drop.task.drop.particle;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class CircleParticle {

    protected final SupplyDropPlugin plugin;
    protected final Location dropLocation;
    protected final double radius;
    protected final int delay;
    private final Task task;

    /**
     * @param particle      particle to spawn
     * @param radius        radius around the player's location to place particles around
     * @param delay         delay between particle spawns
     */
    @SuppressWarnings("java:S107")
    public CircleParticle(SupplyDropPlugin plugin, Location location,
                          double radius, int delay) {
        this.plugin = plugin;
        this.dropLocation = location;
        this.radius = radius;
        this.delay = delay;

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
        private final Particle particle = Particle.REDSTONE;

        @Override
        public void run() {
            for (double i = 0; i < 2 * Math.PI; i += Math.PI / 16) {
                double x = radius * Math.cos(i);
                double z = radius * Math.sin(i);

                // SPAWN RED PARTICLES
                world.spawnParticle(particle, location.clone().add(x, 0, z), 0, 0, 0, 0, 1, new Particle.DustOptions(org.bukkit.Color.RED, 1));
            }
        }

    }

}
