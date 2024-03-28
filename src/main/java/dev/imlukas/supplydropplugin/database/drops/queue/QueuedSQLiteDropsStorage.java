package dev.imlukas.supplydropplugin.database.drops.queue;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.database.impl.sql.statement.IntegerStatementObject;
import dev.imlukas.supplydropplugin.database.impl.sql.statement.StringStatementObject;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.configuration.DropSupplier;
import dev.imlukas.supplydropplugin.database.impl.sql.impl.SQLiteDatabase;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class QueuedSQLiteDropsStorage extends SQLiteDatabase implements QueuedDropsStorage {


    private static final String TABLE_NAME = "queued_drops";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (drop_id VARCHAR(36) NOT NULL PRIMARY KEY, " +
            "type_id VARCHAR(36) NOT NULL, " +
            ");";

    private static final String FETCH_QUEUE = "SELECT * FROM " + TABLE_NAME + ";";
    private static final String STORE_QUEUE = "REPLACE INTO " + TABLE_NAME + " " + "(drop_id, type_id) " + "VALUES (?, ?);";
    private static final String DELETE_QUEUED = "DELETE FROM " + TABLE_NAME + " WHERE drop_id = ?;";

    private final DropSupplier dropSupplier;

    public QueuedSQLiteDropsStorage(SupplyDropPlugin plugin) {
        this.dropSupplier = plugin.getDropSupplier();
    }

    @Override
    protected Collection<String> getTables() {
        return List.of(TABLE_NAME);
    }

    @Override
    protected void createTables() {
        runUpdateAsync(CREATE_TABLE, statement -> {
        });
    }

    @Override
    public CompletableFuture<Queue<Drop>> fetchQueue() {
        return runQueryAsync(FETCH_QUEUE, resultSet -> {
            Queue<Drop> queue = new LinkedList<>();

            while (resultSet.next()) {
                UUID dropUUID = UUID.fromString(resultSet.getString("drop_id"));
                String typeId = resultSet.getString("type_id");
                queue.add(dropSupplier.supplyDrop(typeId, dropUUID));
            }

            return queue;
        });
    }

    @Override
    public CompletableFuture<Void> storeQueue(Queue<Drop> queue) {
        for (Drop drop : queue) {
            runUpdate(STORE_QUEUE,
                    StringStatementObject.create(drop.getUUID().toString()),
                    StringStatementObject.create(drop.getTypeId()));
        }

        return null;
    }

    @Override
    public CompletableFuture<Void> deleteQueued(Drop drop) {
        return runUpdateAsync(DELETE_QUEUED, StringStatementObject.create(drop.getUUID().toString()));
    }
}
