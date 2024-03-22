package dev.imlukas.supplydropplugin.util.core.audience.bukkit;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class BukkitCommandSenderCommandAudience implements CommandAudience {

    private final CommandSender sender;

    public BukkitCommandSenderCommandAudience(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(Component message) {
        sender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }
}
