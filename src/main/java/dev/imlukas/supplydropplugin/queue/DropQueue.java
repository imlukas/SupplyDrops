package dev.imlukas.supplydropplugin.queue;

import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.location.SafeLocation;
import dev.imlukas.supplydropplugin.util.collection.PredicateQueue;

public class DropQueue {

    private final PredicateQueue<Drop> queue = new PredicateQueue<>();

    public void add(Drop drop) {
        queue.add(drop);
    }

    public Drop next(SafeLocation location) {
        return queue.peekIf(drop -> {
            SafeLocation dropLocation = drop.getLocation();
            return location.equals(dropLocation);
        });
    }
}
