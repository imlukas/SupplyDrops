package dev.imlukas.supplydropplugin.drop.commands.config;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.commands.constants.CommandType;
import dev.imlukas.supplydropplugin.drop.commands.registry.DropCommandRegistry;
import dev.imlukas.supplydropplugin.util.file.YMLBase;
import org.bukkit.configuration.ConfigurationSection;

public class CommandConfigHandler extends YMLBase {

    private final DropCommandRegistry commandRegistry;

    public CommandConfigHandler(SupplyDropPlugin plugin) {
        super(plugin, "drops.yml");

        this.commandRegistry = plugin.getCommandRegistry();

        load();
    }

    public void load() {
        ConfigurationSection commands = getConfiguration().getConfigurationSection("commands");

        ConfigurationSection supplyDropCommands = commands.getConfigurationSection("supply-drop");
        if (supplyDropCommands != null) {
            loadCommands(supplyDropCommands, CommandType.SUPPLY_DROP);
        }

        ConfigurationSection ggDropCommands = commands.getConfigurationSection("gg-drop");
        if (ggDropCommands != null) {
            loadCommands(ggDropCommands, CommandType.GG_DROP);
        }
    }

    public void loadCommands(ConfigurationSection section, CommandType type) {
        for (String key : section.getKeys(false)) {
            double chance = section.getDouble(key);

            commandRegistry.registerCommand(type, new ParsedCommand(key, chance));
        }
    }
}
