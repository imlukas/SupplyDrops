package dev.imlukas.supplydropplugin.util.file;

import dev.imlukas.supplydropplugin.util.collection.range.IntegerRange;
import dev.imlukas.supplydropplugin.util.collection.range.Range;
import dev.imlukas.supplydropplugin.util.time.Time;
import lombok.Getter;
import lombok.Setter;
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

    private Particle helixParticle;
    private double helixInitialHeight;
    private double helixMaxHeight;
    private double helixRadius;
    private double helixAngle;
    private double helixIncrement;
    private int helixDelay;

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

        helixParticle = Particle.valueOf(config.getString("helix.particle", "SPELL_WITCH"));
        helixInitialHeight = config.getDouble("helix.initial-height", -0.25);
        helixMaxHeight = config.getDouble("helix.max-height", 3.5);
        helixRadius = config.getDouble("helix.radius", 0.875);
        helixAngle = config.getDouble("helix.angle", 1.75);
        helixIncrement = config.getDouble("helix.increment", 0.075);
        helixDelay = config.getInt("helix.delay", 1);
    }

    public void reload() {
        load();
    }
}
