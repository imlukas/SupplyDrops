package dev.imlukas.supplydropplugin.drop.commands.registry;

import dev.imlukas.supplydropplugin.drop.commands.constants.CommandType;
import dev.imlukas.supplydropplugin.drop.commands.config.ParsedCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropCommandRegistry {

    private final Map<CommandType, List<ParsedCommand>> commands = new HashMap<>();

    public void registerCommand(CommandType type, ParsedCommand command) {
        commands.computeIfAbsent(type, ignored -> new ArrayList<>()).add(command);
    }

    public List<ParsedCommand> getCommands(CommandType type) {
        return commands.get(type);
    }
}
