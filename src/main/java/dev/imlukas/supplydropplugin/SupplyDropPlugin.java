package dev.imlukas.supplydropplugin;

import dev.imlukas.supplydropplugin.commands.ReloadCommand;
import dev.imlukas.supplydropplugin.database.drops.cache.CachedDropsStorage;
import dev.imlukas.supplydropplugin.database.drops.queue.QueuedDropsStorage;
import dev.imlukas.supplydropplugin.database.registry.DatabaseRegistry;
import dev.imlukas.supplydropplugin.drop.tracker.DropTracker;
import dev.imlukas.supplydropplugin.commands.SupplyDropCommand;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.commands.config.CommandConfigHandler;
import dev.imlukas.supplydropplugin.drop.commands.registry.DropCommandRegistry;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.drop.locations.config.LocationConfigHandler;
import dev.imlukas.supplydropplugin.drop.locations.registry.DropLocationRegistry;
import dev.imlukas.supplydropplugin.drop.locations.tracker.DropLocationTracker;
import dev.imlukas.supplydropplugin.drop.task.drop.DropTask;
import dev.imlukas.supplydropplugin.hook.PlaceholderAPIHook;
import dev.imlukas.supplydropplugin.listener.SupplyDropInteractListener;
import dev.imlukas.supplydropplugin.util.core.bukkit.BukkitCommandManager;
import dev.imlukas.supplydropplugin.util.file.PluginSettings;
import dev.imlukas.supplydropplugin.util.file.SoundManager;
import dev.imlukas.supplydropplugin.util.file.messages.Messages;
import dev.imlukas.supplydropplugin.util.io.FileUtils;
import dev.imlukas.supplydropplugin.util.text.Placeholder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

@Getter
public final class SupplyDropPlugin extends JavaPlugin {

    private Messages messages;
    private SoundManager sounds;
    private PluginSettings settings;
    private BukkitCommandManager commandManager;

    private DropCommandRegistry commandRegistry;
    private DropLocationRegistry locationRegistry;
    private DropLocationTracker locationTracker;
    private DropTracker dropTracker;
    private Queue<Drop> dropQueue;
    private DropSupplier dropSupplier;

    private DatabaseRegistry databaseRegistry;
    private CachedDropsStorage cachedDropsStorage;
    private QueuedDropsStorage queuedDropsStorage;
    private DropTask dropTask;

    @Override
    public void onEnable() {
        FileUtils.copyBuiltInResources(this, this.getFile());
        saveDefaultConfig();

        this.messages = new Messages(this);
        this.sounds = new SoundManager(this);
        this.settings = new PluginSettings(this);

        this.commandManager = new BukkitCommandManager(this, messages);

        this.commandRegistry = new DropCommandRegistry();
        new CommandConfigHandler(this).load();

        this.locationRegistry = new DropLocationRegistry();
        new LocationConfigHandler(this).load();

        this.locationTracker = new DropLocationTracker();

        this.dropTracker = new DropTracker();
        this.dropQueue = new LinkedList<>();
        this.dropSupplier = new DropSupplier(this);

        this.databaseRegistry = new DatabaseRegistry(this);

        databaseRegistry.registerDefaults().thenRun(() -> {
            this.cachedDropsStorage = databaseRegistry.getStorage("cached-drops", CachedDropsStorage.class);
            this.queuedDropsStorage = databaseRegistry.getStorage("queued-drops", QueuedDropsStorage.class);

            CompletableFuture.allOf(
                    cachedDropsStorage.fetchCached().thenAccept(dropTracker::addAll),
                    queuedDropsStorage.fetchQueue().thenAccept(dropQueue::addAll)
            ).exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            }).thenRun(() -> {
                System.out.println("Loaded all drops!");
                new SupplyDropCommand(this);
                new ReloadCommand(this);
                registerListener(new SupplyDropInteractListener(this));
                dropTask = new DropTask(this);
            });
        });

        new PlaceholderAPIHook(this).register();
    }

    @Override
    public void onDisable() {
        CompletableFuture.allOf(
                cachedDropsStorage.storeCached(dropTracker.getDrops()),
                queuedDropsStorage.storeQueue(dropQueue)
        ).thenRun(() -> {
            System.out.println("Successfully saved all drops!");
        }).join();
    }

    public void reload() {
        messages.reload();
        sounds.reload();
        settings.reload();
        dropSupplier.reload();

        locationRegistry.clearLocations();
        new LocationConfigHandler(this).load();

        commandRegistry.clearCommands();
        new CommandConfigHandler(this).load();
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
