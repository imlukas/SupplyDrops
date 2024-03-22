package dev.imlukas.supplydropplugin;

import dev.imlukas.supplydropplugin.cache.DropCache;
import dev.imlukas.supplydropplugin.drop.commands.registry.DropCommandRegistry;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.util.file.PluginSettings;
import dev.imlukas.supplydropplugin.util.file.SoundManager;
import dev.imlukas.supplydropplugin.util.file.messages.Messages;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SupplyDropPlugin extends JavaPlugin {

    private Messages messages;
    private SoundManager sounds;
    private PluginSettings settings;

    private DropCommandRegistry commandRegistry;
    private DropCache dropCache;
    private DropSupplier dropSupplier;

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.messages = new Messages(this);
        this.sounds = new SoundManager(this);
        this.settings = new PluginSettings(this);

        this.commandRegistry = new DropCommandRegistry();
        this.dropCache = new DropCache();
        this.dropSupplier = new DropSupplier(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
