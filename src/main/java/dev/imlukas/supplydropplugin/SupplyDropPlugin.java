package dev.imlukas.supplydropplugin;

import dev.imlukas.supplydropplugin.cache.DropCache;
import dev.imlukas.supplydropplugin.commands.ForceDropCommand;
import dev.imlukas.supplydropplugin.drop.commands.config.CommandConfigHandler;
import dev.imlukas.supplydropplugin.drop.commands.registry.DropCommandRegistry;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
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

@Getter
public final class SupplyDropPlugin extends JavaPlugin {

    private Messages messages;
    private SoundManager sounds;
    private PluginSettings settings;
    private BukkitCommandManager commandManager;

    private DropCommandRegistry commandRegistry;
    private DropCache dropCache;
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
        this.dropCache = new DropCache();
        this.dropSupplier = new DropSupplier(this);

        new ForceDropCommand(this);

        registerListener(new SupplyDropInteractListener(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
