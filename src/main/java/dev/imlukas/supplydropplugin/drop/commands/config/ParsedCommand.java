package dev.imlukas.supplydropplugin.drop.commands.config;

import lombok.Getter;

@Getter
public class ParsedCommand {

    private final String command;
    private final double chance;

    public ParsedCommand(String command, double chance) {
        this.command = command;
        this.chance = chance;
    }
}
