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

    private long timePerDrop;
    private IntegerRange supplyDropCommandRange;
    private IntegerRange ggDropCommandQuantityRange;

    private final FileConfiguration config;

    public PluginSettings(JavaPlugin plugin) {
        super(plugin, "settings.yml");
        this.config = getConfiguration();
        load();
    }

    public void load() {
        timePerDrop = Time.fromString(config.getString("time-per-supply-drop")).asTicks();

        int supplyDropMin = config.getInt("ranges.supply-drop.min");
        int supplyDropMax = config.getInt("ranges.supply-drop.max");
        supplyDropCommandRange = Range.ofInteger(supplyDropMin, supplyDropMax);

        int ggDropMin = config.getInt("ranges.gg-drop.min");
        int ggDropMax = config.getInt("ranges.gg-drop.max");
        ggDropCommandQuantityRange = Range.ofInteger(ggDropMin, ggDropMax);
    }

    public void reload() {
        load();
    }
}
