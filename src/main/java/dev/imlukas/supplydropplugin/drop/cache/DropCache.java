package dev.imlukas.supplydropplugin.drop.cache;

import dev.imlukas.supplydropplugin.drop.Drop;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Stores {@link Drop} objects until they're interacted with
 */
public class DropCache {

    private final Map<UUID, Drop> drops = new HashMap<>();

    public void add(Drop supplyDrop) {
        drops.put(supplyDrop.getUUID(), supplyDrop);
    }

    public void remove(UUID uuid) {
        drops.remove(uuid);
    }

    public Map<UUID, Drop> getCached() {
        return drops;
    }

    public Drop getById(UUID uuid) {
        return drops.get(uuid);
    }

    public Drop getByEntityId(UUID entityId) {
        for (Drop value : drops.values()) {
            if (!value.getEntityId().equals(entityId)) {
                continue;
            }

            return value;
        }

        return null;
    }
}
