package dev.imlukas.supplydropplugin.util.file;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
@Setter
public class PluginSettings {

    private final FileConfiguration config;
    public PluginSettings(JavaPlugin plugin) {
        this.config = plugin.getConfig();
        load();
    }

    public void load() {
    }

    public void reload() {
        load();
    }
}
