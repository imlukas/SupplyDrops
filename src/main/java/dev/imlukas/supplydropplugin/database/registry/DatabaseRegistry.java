package dev.imlukas.supplydropplugin.database.registry;

import eu.merakistudios.fishingtraps.FishingTrapsPlugin;
import dev.imlukas.supplydropplugin.database.Database;
import dev.imlukas.supplydropplugin.database.fishing.biomes.unlocked.SQLiteUnlockedBiomesStorage;
import dev.imlukas.supplydropplugin.database.fishing.biomes.active.SQLiteActiveBiomesStorage;
import dev.imlukas.supplydropplugin.database.fishing.traps.SQLiteTrapStorage;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DatabaseRegistry {

    private final FishingTrapsPlugin plugin;
    private final Map<String, RegisteredDatabase> databases = new HashMap<>();

    public DatabaseRegistry(FishingTrapsPlugin plugin) {
        this.plugin = plugin;
    }

    public CompletableFuture<Void> registerDefaults() {
        return CompletableFuture.allOf(
                register("biomes-caught", DatabaseProvider.of(new dev.imlukas.supplydropplugin.database.fishing.biomes.caught.SQLiteBiomesStorage())),
                register("biomes-active", DatabaseProvider.of(new SQLiteActiveBiomesStorage(plugin))),
                register("biomes-unlocked", DatabaseProvider.of(new SQLiteUnlockedBiomesStorage())),
                register("fishing-traps", DatabaseProvider.of(new SQLiteTrapStorage(plugin)))
        ).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

    }

    public CompletableFuture<Void> register(String name, DatabaseProvider provider) {
        databases.put(name, new RegisteredDatabase(name, provider));
        return tryLoad(databases.get(name));
    }

    public <T extends Database> T getStorage(String name, Class<T> databaseClass) {
        RegisteredDatabase registeredDatabase = databases.get(name);

        if (registeredDatabase == null) {
            System.out.println("Database " + name + " is not registered!");
            return null;
        }

        Database database = registeredDatabase.getDatabase();

        if (database == null) {
            return null;
        }

        if (databaseClass.isInstance(database)) {
            return databaseClass.cast(database);
        }

        return null;
    }

    public CompletableFuture<Void> tryLoad(RegisteredDatabase database) {
        if (database.isEnabled()) {
            return CompletableFuture.completedFuture(null);
        }

        System.out.println(plugin.getConfig().getKeys(true));

        String databaseType = plugin.getConfig().getString("databases." + database.getName() + ".type");
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("databases." + database.getName() + ".credentials");

        if (section == null) {
            System.out.println("Failed to load database " + database.getName() + " because credentials are not set!");
            return CompletableFuture.completedFuture(null);
        }

        Database toEnable = database.getProvider().getDatabase(databaseType);
        return toEnable.enable(plugin, section).thenAccept(value -> {
            if (value) {
                database.setEnabled(true);
                database.setDatabase(toEnable);
            } else {
                System.out.println("Failed to enable database " + database.getName());
            }
        });
    }
}
