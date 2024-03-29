package dev.imlukas.supplydropplugin.drop.locations.config;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.locations.registry.DropLocationRegistry;
import dev.imlukas.supplydropplugin.drop.locations.DropLocation;
import dev.imlukas.supplydropplugin.util.file.YMLBase;
import org.bukkit.configuration.ConfigurationSection;

public class LocationConfigHandler extends YMLBase {

    private final DropLocationRegistry locationRegistry;

    public LocationConfigHandler(SupplyDropPlugin plugin) {
        super(plugin, "drops.yml");
        this.locationRegistry = plugin.getLocationRegistry();
    }

    public void load() {
        for (String dropId : getConfiguration().getKeys(false)) {
            ConfigurationSection dropSection = getConfiguration().getConfigurationSection(dropId);

            if (dropSection == null) {
                System.out.println("Invalid drop section for " + dropId);
                continue;
            }

            System.out.println("Loading locations for " + dropId);
            loadLocations(dropSection.getConfigurationSection("locations"), dropId);
        }
    }

    public void loadLocations(ConfigurationSection section, String id) {
        for (String key : section.getKeys(false)) {
            ConfigurationSection locationSection = section.getConfigurationSection(key);

            locationRegistry.registerLocation(id, DropLocation.fromSection(locationSection));
        }
    }
}
