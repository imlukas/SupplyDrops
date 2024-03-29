package dev.imlukas.supplydropplugin.drop.locations;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

@Getter
public class DropLocation {

    private final UUID worldId;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    private boolean isOccupied = false;

    public DropLocation(Location location) {
        this.worldId = location.getWorld().getUID();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldId);
    }

    public static DropLocation fromLocation(Location location) {
        return new DropLocation(location);
    }

    public static DropLocation fromSection(ConfigurationSection locationSection) {
        if (locationSection == null) {
            return null;
        }

        World world = Bukkit.getWorld(locationSection.getString("world", "world"));
        double x = locationSection.getDouble("x");
        double y = locationSection.getDouble("y");
        double z = locationSection.getDouble("z");
        float yaw = (float) locationSection.getDouble("yaw", 0d);
        float pitch = (float) locationSection.getDouble("pitch", 0d);

        return fromLocation(new Location(world, x, y, z, yaw, pitch));
    }


    public boolean equals(DropLocation location) {
        return location.getWorldId().equals(getWorldId())
                && location.getX() == getX()
                && location.getY() == getY()
                && location.getZ() == getZ();
    }

    public Location asBukkitLocation() {
        return new Location(Bukkit.getWorld(worldId), x, y, z, yaw, pitch);
    }

    public static DropLocation deserialize(String location) {
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

    public String serialize() {
        return Bukkit.getWorld(worldId).getName() + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
    }
}
