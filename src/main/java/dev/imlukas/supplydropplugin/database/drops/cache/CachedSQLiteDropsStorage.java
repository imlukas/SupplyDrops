package dev.imlukas.supplydropplugin.database.drops.cache;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.database.impl.sql.impl.SQLiteDatabase;
import dev.imlukas.supplydropplugin.database.impl.sql.statement.StringStatementObject;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.commands.CommandAction;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.drop.impl.ConfigurableDrop;
import dev.imlukas.supplydropplugin.drop.locations.registry.DropLocationRegistry;
import dev.imlukas.supplydropplugin.drop.locations.DropLocation;
import dev.imlukas.supplydropplugin.drop.locations.tracker.DropLocationTracker;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CachedSQLiteDropsStorage extends SQLiteDatabase implements CachedDropsStorage {


    private static final String TABLE_NAME = "cached_drops";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (drop_id VARCHAR(36) NOT NULL PRIMARY KEY, " +
            "type_id VARCHAR(36) NOT NULL, " +
            "entity_id VARCHAR(36) NOT NULL, " +
            "commands TEXT NOT NULL, " +
            "location TEXT NOT NULL" +
            ");";

    private static final String FETCH_CACHED = "SELECT * FROM " + TABLE_NAME + ";";
    private static final String STORE_CACHED = "REPLACE INTO " + TABLE_NAME + " " + "(drop_id, type_id, entity_id, commands, location) " + "VALUES (?, ?, ?, ?, ?);";
    private static final String DELETE_CACHED = "DELETE FROM " + TABLE_NAME;

    private final SupplyDropPlugin plugin;
    private final DropLocationRegistry locationRegistry;
    private final DropLocationTracker locationTracker;
    private final DropSupplier dropSupplier;

    public CachedSQLiteDropsStorage(SupplyDropPlugin plugin) {
        this.plugin = plugin;
        this.locationRegistry = plugin.getLocationRegistry();
        this.locationTracker = plugin.getLocationTracker();
        this.dropSupplier = plugin.getDropSupplier();
    }

    @Override
    protected Collection<String> getTables() {
        return List.of(TABLE_NAME);
    }

    @Override
    protected void createTables() {
        runUpdateAsync(CREATE_TABLE);
    }

    @Override
    public CompletableFuture<List<Drop>> fetchCached() {
        return runQueryAsync(FETCH_CACHED, resultSet -> {
            List<Drop> tracked = new ArrayList<>();

            while (resultSet.next()) {
                UUID dropUUID = UUID.fromString(resultSet.getString("drop_id"));
                String typeId = resultSet.getString("type_id");
                UUID entityId = UUID.fromString(resultSet.getString("entity_id"));
                String location = resultSet.getString("location");

                DropLocation dropLocation = locationRegistry.getFromSerialized(location);

                if (dropLocation != null) {
                    dropLocation.setOccupied(true);
                }

                String commands = resultSet.getString("commands");
                CommandAction commandAction = CommandAction.deserialize(commands);

                ConfigurationSection configurationSection = dropSupplier.getDropConfiguration(typeId);
                String displayName = configurationSection.getString("stand-name");
                String modelId = configurationSection.getString("model");

                ConfigurableDrop drop = ConfigurableDrop.create(plugin, dropUUID, typeId, displayName, modelId, commandAction);
                drop.setEntityId(entityId);
                tracked.add(drop);
            }

            return tracked;
        });
    }

    @Override
    public CompletableFuture<Void> storeCached(List<Drop> cached) {
        return runUpdateAsync(DELETE_CACHED).thenRun(() -> {
            cached.forEach(drop -> {
                runUpdateAsync(STORE_CACHED,
                        StringStatementObject.create(drop.getUUID().toString()),
                        StringStatementObject.create(drop.getTypeId()),
                        StringStatementObject.create(drop.getEntityId().toString()),
                        StringStatementObject.create(drop.getCommands().serialize()),
                        StringStatementObject.create(locationTracker.get(drop).serialize()));
            });
        });
    }

    @Override
    public CompletableFuture<Void> deleteCached(Drop drop) {
        return runUpdateAsync(DELETE_CACHED);
    }
}
