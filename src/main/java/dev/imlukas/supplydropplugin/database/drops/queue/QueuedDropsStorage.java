package dev.imlukas.supplydropplugin.database.drops.queue;

import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.database.Database;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public interface QueuedDropsStorage extends Database {

    CompletableFuture<Queue<Drop>> fetchQueue();

    CompletableFuture<Void> storeQueue(Queue<Drop> queue);

    CompletableFuture<Void> deleteQueued(Drop drop);
}
