package dev.imlukas.supplydropplugin.drop.commands.registry;

import dev.imlukas.supplydropplugin.drop.commands.constants.CommandType;
import dev.imlukas.supplydropplugin.drop.commands.config.ParsedCommand;
import dev.imlukas.supplydropplugin.util.collection.CollectionUtils;
import dev.imlukas.supplydropplugin.util.collection.range.IntegerRange;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DropCommandRegistry {

    private final EnumMap<CommandType, List<ParsedCommand>> commands = new EnumMap<>(CommandType.class);

    public void registerCommand(CommandType type, ParsedCommand command) {
        System.out.println("Registering command: " + command.getCommand() + " with type: " + type);
        commands.computeIfAbsent(type, ignored -> new ArrayList<>()).add(command);
    }

    public List<String> getRandom(CommandType type, IntegerRange range) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        List<ParsedCommand> commandList = this.commands.get(type);
        List<String> selectedCommands = new ArrayList<>();

        int min = range.min();
        int max = range.max();

        for (int i = 0; i < max; i++) {
            ParsedCommand command = CollectionUtils.getRandom(commandList);

            if (command == null) {
                System.out.println("No commands found for type " + type);
                continue;
            }

            if (random.nextDouble() >= command.getChance()) {
                System.out.println("Command " + command.getCommand() + " failed chance check");
                continue;
            }

            System.out.println("Adding command " + command.getCommand() + " to list");
            selectedCommands.add(command.getCommand());
        }

        if (selectedCommands.size() < min) {
            System.out.println("Not enough commands, adding random commands");
            int diff = min - selectedCommands.size();

            for (int i = 0; i < diff; i++) {
                ParsedCommand command = CollectionUtils.getRandom(commandList);

                if (command == null) {
                    continue;
                }

                selectedCommands.add(command.getCommand());
            }
        }

        System.out.println("Selected commands: " + selectedCommands.toString());
        return selectedCommands;
    }

    public List<ParsedCommand> getCommands(CommandType type) {
        return commands.get(type);
    }
}
