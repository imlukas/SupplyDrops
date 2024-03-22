package dev.imlukas.supplydropplugin.cache;

import dev.imlukas.supplydropplugin.drop.impl.SupplyDrop;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DropCache {

    private final Map<UUID, SupplyDrop> supplyDrops = new HashMap<>();

    public void add(UUID uuid, SupplyDrop supplyDrop) {
        supplyDrops.put(uuid, supplyDrop);
    }

    public void remove(UUID uuid) {
        supplyDrops.remove(uuid);
    }

    public SupplyDrop get(UUID uuid) {
        return supplyDrops.get(uuid);
    }
}
