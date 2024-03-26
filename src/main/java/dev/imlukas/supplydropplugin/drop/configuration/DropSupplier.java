package dev.imlukas.supplydropplugin.drop.configuration;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.impl.ConfigurableDrop;
import dev.imlukas.supplydropplugin.util.file.YMLBase;
import org.bukkit.configuration.ConfigurationSection;

public class DropSupplier extends YMLBase {

    private final SupplyDropPlugin plugin;

    public DropSupplier(SupplyDropPlugin plugin) {
        super(plugin, "drops.yml");

        this.plugin = plugin;
    }

    public ConfigurableDrop supplyDrop(String id) {
        ConfigurationSection dropSection = getConfiguration().getConfigurationSection(id);
        return ConfigurableDrop.create(plugin, dropSection);
    }
}
