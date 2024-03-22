package dev.imlukas.supplydropplugin.util.core.audience.bukkit;

import org.bukkit.command.CommandSender;

/**
 * Represents a Bukkit console audience.
 */
public class BukkitConsoleCommandAudience extends BukkitCommandSenderCommandAudience {

    public BukkitConsoleCommandAudience(CommandSender sender) {
        super(sender);
    }

    @Override
    public boolean isConsole() {
        return true;
    }
}
