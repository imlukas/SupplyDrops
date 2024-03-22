package dev.imlukas.supplydropplugin.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class SafeLocation {

    private final UUID worldId;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public SafeLocation(Location location) {
        this.worldId = location.getWorld().getUID();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public World getWorld() {
        return Bukkit.getWorld(worldId);
    }


    public static SafeLocation fromLocation(Location location) {
        return new SafeLocation(location);
    }

    public static SafeLocation fromSection(ConfigurationSection locationSection) {
        if (locationSection == null) {
            return null;
        }

        World world = Bukkit.getWorld(UUID.fromString(locationSection.getString("world", "world")));
        double x = locationSection.getDouble("x");
        double y = locationSection.getDouble("y");
        double z = locationSection.getDouble("z");
        float yaw = (float) locationSection.getDouble("yaw", 0d);
        float pitch = (float) locationSection.getDouble("pitch", 0d);

        return fromLocation(new Location(world, x, y, z, yaw, pitch));
    }

    public static SafeLocation fromString(String location) {
        if (location == null) {
            return null;
        }

        String[] split = location.split(":");

        World world = Bukkit.getWorld(split[0]);

        if (world == null) {
            return null;
        }

        return fromLocation(new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5])));
    }

    public Location asBukkitLocation() {
        return new Location(Bukkit.getWorld(worldId), x, y, z, yaw, pitch);
    }

    public String serialize() {
        return Bukkit.getWorld(worldId).getName() + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
    }
}
