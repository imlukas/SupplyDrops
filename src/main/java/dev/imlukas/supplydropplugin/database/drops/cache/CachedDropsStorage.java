package dev.imlukas.supplydropplugin.database.drops.cache;

import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.database.Database;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CachedDropsStorage extends Database {

    CompletableFuture<List<Drop>> fetchCached();

    CompletableFuture<Void> storeCached(List<Drop> cached);

    CompletableFuture<Void> deleteCached(Drop drop);
}
