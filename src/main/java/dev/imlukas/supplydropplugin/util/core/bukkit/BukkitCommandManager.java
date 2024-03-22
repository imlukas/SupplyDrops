package dev.imlukas.supplydropplugin.util.core.bukkit;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import dev.imlukas.supplydropplugin.util.core.manager.AbstractCommandManager;
import dev.imlukas.supplydropplugin.util.file.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BukkitCommandManager extends AbstractCommandManager {

    private static final CommandMap COMMAND_MAP;
    private static final Constructor<PluginCommand> PLUGIN_COMMAND_CONSTRUCTOR;

    private static final Method REGISTER_SERVER_ALIASES_METHOD;
    private static final Method SYNC_COMMANDS_METHOD;
    private static final Method INITIALIZE_HELPMAP_COMMANDS;

    static {
        try {
            Class<?> serverClass = Bukkit.getServer().getClass();
            Field commandMapField = serverClass.getDeclaredField("commandMap");

            commandMapField.setAccessible(true);

            COMMAND_MAP = (CommandMap) commandMapField.get(Bukkit.getServer());

            Class<PluginCommand> pluginCommandClass = PluginCommand.class;

            PLUGIN_COMMAND_CONSTRUCTOR = pluginCommandClass.getDeclaredConstructor(String.class, Plugin.class);
            PLUGIN_COMMAND_CONSTRUCTOR.setAccessible(true);

            SYNC_COMMANDS_METHOD = serverClass.getDeclaredMethod("syncCommands");
            SYNC_COMMANDS_METHOD.setAccessible(true);

            REGISTER_SERVER_ALIASES_METHOD = commandMapField.getType().getSuperclass().getDeclaredMethod("registerServerAliases");
            REGISTER_SERVER_ALIASES_METHOD.setAccessible(true);

            Class<?> helpMapClass = Bukkit.getHelpMap().getClass();
            INITIALIZE_HELPMAP_COMMANDS = helpMapClass.getDeclaredMethod("initializeCommands");
            INITIALIZE_HELPMAP_COMMANDS.setAccessible(true);
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final JavaPlugin plugin;

    public BukkitCommandManager(JavaPlugin plugin, Messages messages) {
        super(messages);
        this.plugin = plugin;
    }

    @Override
    public void registerRoot(String name) {
        BukkitCommand command = new BukkitCommand();

        try {
            PluginCommand pluginCommand = PLUGIN_COMMAND_CONSTRUCTOR.newInstance(name, plugin);

            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);

            COMMAND_MAP.register(name, pluginCommand);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void syncCommands() {
        try {
            REGISTER_SERVER_ALIASES_METHOD.invoke(COMMAND_MAP);
            SYNC_COMMANDS_METHOD.invoke(Bukkit.getServer());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private class BukkitCommand implements CommandExecutor, TabCompleter {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
            CommandAudience commandAudience = BukkitAudiences.getAudience(sender);
            handle(commandAudience, s, strings);
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
            CommandAudience commandAudience = BukkitAudiences.getAudience(sender);
            return tabComplete(commandAudience, s, strings);
        }
    }
}
