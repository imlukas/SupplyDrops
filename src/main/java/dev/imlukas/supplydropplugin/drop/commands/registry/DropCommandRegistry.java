package dev.imlukas.supplydropplugin.drop.commands.registry;

import dev.imlukas.supplydropplugin.drop.commands.config.ParsedCommand;
import dev.imlukas.supplydropplugin.util.collection.CollectionUtils;
import dev.imlukas.supplydropplugin.util.collection.range.IntegerRange;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DropCommandRegistry {

    private final HashMap<String, List<ParsedCommand>> commands = new HashMap<>();

    public void registerCommand(String dropId, ParsedCommand command) {
        System.out.println("Registering command for " + dropId + ": " + command.getCommand());
        commands.computeIfAbsent(dropId, ignored -> new ArrayList<>()).add(command);
    }

    public List<String> getRandom(String id, IntegerRange range) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        List<ParsedCommand> commandList = this.commands.get(id);
        List<String> selectedCommands = new ArrayList<>();

        int min = range.min();
        int max = range.max();

        for (int i = 0; i < max; i++) {
            ParsedCommand command = CollectionUtils.getRandom(commandList);

            if (command == null) {
                continue;
            }

            if (random.nextDouble() >= command.getChance()) {
                continue;
            }

            selectedCommands.add(command.getCommand());
        }

        if (selectedCommands.size() < min) {
            int diff = min - selectedCommands.size();

            for (int i = 0; i < diff; i++) {
                ParsedCommand command = CollectionUtils.getRandom(commandList);

                if (command == null) {
                    continue;
                }

                selectedCommands.add(command.getCommand());
            }
        }

        return selectedCommands;
    }

    public List<ParsedCommand> getCommands(String dropId) {
        return commands.get(dropId);
    }

    public void clearCommands() {
        commands.clear();
    }
}
