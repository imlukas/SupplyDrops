package dev.imlukas.supplydropplugin;

import dev.imlukas.supplydropplugin.drop.cache.DropCache;
import dev.imlukas.supplydropplugin.commands.SupplyDropCommand;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.commands.config.CommandConfigHandler;
import dev.imlukas.supplydropplugin.drop.commands.registry.DropCommandRegistry;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.drop.locations.config.LocationConfigHandler;
import dev.imlukas.supplydropplugin.drop.locations.registry.DropLocationRegistry;
import dev.imlukas.supplydropplugin.drop.task.drop.DropTask;
import dev.imlukas.supplydropplugin.listener.SupplyDropInteractListener;
import dev.imlukas.supplydropplugin.util.core.bukkit.BukkitCommandManager;
import dev.imlukas.supplydropplugin.util.file.PluginSettings;
import dev.imlukas.supplydropplugin.util.file.SoundManager;
import dev.imlukas.supplydropplugin.util.file.messages.Messages;
import dev.imlukas.supplydropplugin.util.io.FileUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.Queue;

@Getter
public final class SupplyDropPlugin extends JavaPlugin {

    private Messages messages;
    private SoundManager sounds;
    private PluginSettings settings;
    private BukkitCommandManager commandManager;

    private DropCommandRegistry commandRegistry;
    private DropLocationRegistry locationRegistry;
    private DropCache dropCache;
    private Queue<Drop> dropQueue;
    private DropSupplier dropSupplier;

    @Override
    public void onEnable() {
        FileUtils.copyBuiltInResources(this, this.getFile());

        this.messages = new Messages(this);
        this.sounds = new SoundManager(this);
        this.settings = new PluginSettings(this);

        this.commandManager = new BukkitCommandManager(this, messages);

        this.commandRegistry = new DropCommandRegistry();
        new CommandConfigHandler(this).load();

        this.locationRegistry = new DropLocationRegistry();
        new LocationConfigHandler(this).load();

        this.dropCache = new DropCache();
        this.dropQueue = new LinkedList<>();
        this.dropSupplier = new DropSupplier(this);

        new SupplyDropCommand(this);

        registerListener(new SupplyDropInteractListener(this));

        new DropTask(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
