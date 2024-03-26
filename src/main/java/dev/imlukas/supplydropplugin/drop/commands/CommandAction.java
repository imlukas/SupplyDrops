package dev.imlukas.supplydropplugin.drop.commands;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandAction {

    private static final Pattern PATTERN = Pattern.compile("\\[(.*)]");
    private final List<String> commands = new ArrayList<>();

    public CommandAction(List<String> commands) {
        this.commands.addAll(commands);
    }

    public static CommandAction create(List<String> commands) {
        return new CommandAction(commands);
    }

    public static CommandAction create(ConfigurationSection section) {
        return new CommandAction(section.getStringList("commands"));
    }

    public List<String> getCommands() {
        return commands;
    }

    public void execute(Player player) {
        System.out.println("Executing commands for " + player.getName());
        for (String command : commands) {
            Matcher matcher = PATTERN.matcher(command);

            if (!matcher.find()) {
                player.performCommand(command.replace("%player%", player.getName()));
                return;
            }

            String commandSender = matcher.group(1);
            String toExecute = command.replace("[" + commandSender + "] ", "").replace("%player%", player.getName());

            if (commandSender.equalsIgnoreCase("console")) {
                player.getServer().dispatchCommand(Bukkit.getConsoleSender(), toExecute);
            } else {
                player.performCommand(toExecute);
            }

        }
    }
}
