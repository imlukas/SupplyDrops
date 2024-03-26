package dev.imlukas.supplydropplugin.drop.configuration;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.commands.CommandAction;
import dev.imlukas.supplydropplugin.drop.commands.constants.CommandType;
import dev.imlukas.supplydropplugin.drop.commands.registry.DropCommandRegistry;
import dev.imlukas.supplydropplugin.drop.impl.SupplyDrop;
import dev.imlukas.supplydropplugin.location.SafeLocation;
import dev.imlukas.supplydropplugin.util.collection.CollectionUtils;
import dev.imlukas.supplydropplugin.util.collection.range.IntegerRange;
import dev.imlukas.supplydropplugin.util.file.PluginSettings;
import dev.imlukas.supplydropplugin.util.file.YMLBase;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;

public class DropSupplier extends YMLBase {

    private final PluginSettings settings;
    private final DropCommandRegistry commandRegistry;

    public DropSupplier(SupplyDropPlugin plugin) {
        super(plugin, "drops.yml");

        this.settings = plugin.getSettings();
        this.commandRegistry = plugin.getCommandRegistry();
    }

    public Drop supplyDrop() {
        System.out.println("Supplying drop");
        IntegerRange range = settings.getSupplyDropCommandRange();
        ConfigurationSection locations = getConfiguration().getConfigurationSection("locations");
        Set<String> keys = locations.getKeys(false);

        String randomLocation = CollectionUtils.getRandom(keys);
        SafeLocation location = SafeLocation.fromSection(locations.getConfigurationSection(randomLocation));

        CommandAction commands =  CommandAction.create(commandRegistry.getRandom(CommandType.SUPPLY_DROP, range));

        System.out.println(commands.getCommands().toString());

        return SupplyDrop.create(commands, location);
    }
}
