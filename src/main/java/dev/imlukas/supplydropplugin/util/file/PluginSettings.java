package dev.imlukas.supplydropplugin.util.file;

import dev.imlukas.supplydropplugin.util.collection.range.IntegerRange;
import dev.imlukas.supplydropplugin.util.collection.range.Range;
import dev.imlukas.supplydropplugin.util.time.Time;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public class PluginSettings extends YMLBase{

    private double dropVelocity;
    private String defaultDropId;
    private long timePerDrop;

    private final FileConfiguration config;

    public PluginSettings(JavaPlugin plugin) {
        super(plugin, "settings.yml");
        this.config = getConfiguration();
        load();
    }

    public void load() {
        dropVelocity = config.getDouble("drop-velocity-multiplier", -1.0d);
        defaultDropId = config.getString("default-drop", "supply-drop");
        timePerDrop = Time.fromString(config.getString("time-per-supply-drop", "30 minutes")).asTicks();
    }

    public void reload() {
        load();
    }
}
