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
    }

    public void load() {
        System.out.println("Loading commands");
        ConfigurationSection commands = getConfiguration().getConfigurationSection("commands");

        for (String commandType : commands.getKeys(false)) {
            CommandType type = CommandType.valueOf(commandType.toUpperCase());
            ConfigurationSection commandSection = commands.getConfigurationSection(commandType);

            if (commandSection == null) {
                System.out.println("Invalid command section for " + type);
                continue;
            }

            System.out.println("Loading commands for " + type);
            loadCommands(commandSection, type);
        }
    }

    public void loadCommands(ConfigurationSection section, CommandType type) {
        for (String key : section.getKeys(false)) {

            String command = section.getString(key + ".command");
            double chance = section.getDouble(key + ".chance");

            commandRegistry.registerCommand(type, new ParsedCommand(command, chance));
        }
    }
}
