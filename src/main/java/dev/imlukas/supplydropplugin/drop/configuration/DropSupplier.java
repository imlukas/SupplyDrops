package dev.imlukas.supplydropplugin.drop.configuration;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.impl.ConfigurableDrop;
import dev.imlukas.supplydropplugin.util.file.YMLBase;
import org.bukkit.configuration.ConfigurationSection;
import org.checkerframework.checker.units.qual.C;

import java.util.UUID;

public class DropSupplier extends YMLBase {

    private final SupplyDropPlugin plugin;

    public DropSupplier(SupplyDropPlugin plugin) {
        super(plugin, "drops.yml");

        this.plugin = plugin;
    }

    public ConfigurationSection getDropConfiguration(String id) {
        return getConfiguration().getConfigurationSection(id);
    }

    public ConfigurableDrop supplyDrop(String id, UUID uuid) {
        ConfigurationSection dropSection = getConfiguration().getConfigurationSection(id);
        return ConfigurableDrop.create(plugin, uuid, dropSection);
    }

    public ConfigurableDrop supplyDrop(String id) {
        return supplyDrop(id, UUID.randomUUID());
    }
}
