package dev.imlukas.supplydropplugin.util.file;

import dev.imlukas.supplydropplugin.util.time.Time;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PluginSettings extends YMLBase{

    private double dropVelocity;
    private String defaultDropId;
    private int playerLocationHeight;
    private long timePerDrop;

    private List<Long> notificationTicks = new ArrayList<>();

    private Particle circleParticle;
    private double circleRadius;
    private int circleDelay;

    private final FileConfiguration config;

    public PluginSettings(JavaPlugin plugin) {
        super(plugin, "settings.yml");
        this.config = getConfiguration();
        load();
    }

    public void load() {
        dropVelocity = config.getDouble("drop-velocity-multiplier", -1.0d);
        defaultDropId = config.getString("default-drop", "supply-drop");
        playerLocationHeight = config.getInt("player-location-height", 150);
        timePerDrop = Time.fromString(config.getString("time-per-supply-drop", "30 minutes")).asTicks();

        List<String> notificationTimes = config.getStringList("notification-times");
        for (String notificationTime : notificationTimes) {
            Time time = Time.fromString(notificationTime);

            if (time == null) {
                continue;
            }

            notificationTicks.add(time.asTicks());
        }

        circleRadius = config.getDouble("circle.radius", 0.875);
        circleDelay = config.getInt("circle.delay", 1);
    }

    public void reload() {
        load();
    }
}
