package dev.imlukas.supplydropplugin.drop.task.drop.particle;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RainbowCircleParticle extends CircleParticle {

    private final Task task;
    private static final int POINT_AMOUNT = 32;
    private static final List<Color> colors = Arrays.asList(
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE
    );

    private int frame;

    /**
     * @param radius        radius around the player's location to place particles around
     * @param delay         delay between particle spawns
     */
    @SuppressWarnings("java:S107")
    public RainbowCircleParticle(SupplyDropPlugin plugin, Location location,
                                 double radius, int delay) {
        super(plugin, location, radius, delay);
        this.task = new Task();
    }


    @Override
    public void start() {
        this.task.runTaskTimerAsynchronously(plugin, 0, delay);
    }

    @Override
    public void stop() {
        this.task.cancel();
    }

    private class Task extends BukkitRunnable {

        private final Location location = dropLocation;
        private final World world = location.getWorld();

        @Override
        public void run() {
            frame = 0;

            // make a rotating rainbow circle by applying offset into index
            // frame = i * (255*3 / POINT_AMOUNT);
            for (int i = 0; i < POINT_AMOUNT; i++) {
                double angle = 2 * Math.PI * i / POINT_AMOUNT;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);

                world.spawnParticle(Particle.REDSTONE, location.clone().add(x, 0, z), 0, 0, 0, 0, 1, new Particle.DustOptions(renderColor(), 1));
                frame = i * (255*3 / POINT_AMOUNT);
            }
        }

    }

    private Color renderColor() {
        // Sum of RGB is 255, so it shifts from red to green to blue
        // Fully transitioned to green = 255
        if(frame < 255) {
            return Color.fromRGB(255 - frame, frame, 0);
        }

        // Fully transitioned to blue = 255 * 2
        if(frame < 255 * 2) {
            return Color.fromRGB(0, 255 - (frame - 255), frame - 255);
        }

        // Fully transitioned to red = 255 * 3
        return Color.fromRGB(frame - 255 * 2, 0, 255 - (frame - 255 * 2));
    }

}
