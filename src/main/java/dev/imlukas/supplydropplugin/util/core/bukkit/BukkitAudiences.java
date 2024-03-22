package dev.imlukas.supplydropplugin.util.core.bukkit;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import dev.imlukas.supplydropplugin.util.core.audience.bukkit.BukkitCommandSenderCommandAudience;
import dev.imlukas.supplydropplugin.util.core.audience.bukkit.BukkitConsoleCommandAudience;
import dev.imlukas.supplydropplugin.util.core.audience.bukkit.BukkitPlayerCommandAudience;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class BukkitAudiences {

    public static CommandAudience getAudience(CommandSender sender) {
        // I can't control the impl of CommandSender, so I can't make a createAudience method.

        if (sender instanceof Player) {
            return new BukkitPlayerCommandAudience((Player) sender);
        }

        if (sender instanceof ConsoleCommandSender) {
            return new BukkitConsoleCommandAudience(sender);
        }

        return new BukkitCommandSenderCommandAudience(sender);
    }


}
