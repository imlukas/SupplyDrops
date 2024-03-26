package dev.imlukas.supplydropplugin.drop.commands.config;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
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
        for (String dropId : getConfiguration().getKeys(false)) {
            ConfigurationSection dropSection = getConfiguration().getConfigurationSection(dropId);

            if (dropSection == null) {
                System.out.println("Invalid drop section for " + dropId);
                continue;
            }

            ConfigurationSection commands = dropSection.getConfigurationSection("commands");
            System.out.println("Loading commands for " + dropId);
            loadCommands(commands, dropId);
        }
    }

    public void loadCommands(ConfigurationSection section, String id) {
        for (String key : section.getKeys(false)) {

            String command = section.getString(key + ".command");
            double chance = section.getDouble(key + ".chance");

            commandRegistry.registerCommand(id, new ParsedCommand(command, chance));
        }
    }
}
