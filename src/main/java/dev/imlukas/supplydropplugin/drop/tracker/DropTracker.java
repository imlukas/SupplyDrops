package dev.imlukas.supplydropplugin.drop.tracker;

import dev.imlukas.supplydropplugin.drop.Drop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Stores {@link Drop} objects until they're interacted with
 */
public class DropTracker {

    private final Map<UUID, Drop> drops = new HashMap<>();

    public void add(Drop supplyDrop) {
        System.out.println("Adding drop " + supplyDrop.getUUID() + " to tracker");
        drops.put(supplyDrop.getUUID(), supplyDrop);
    }

    public void addAll(List<Drop> drops) {
        for (Drop drop : drops) {
            add(drop);
        }
    }

    public void remove(UUID uuid) {
        drops.remove(uuid);
    }

    public Map<UUID, Drop> getTracked() {
        return drops;
    }

    public List<Drop> getDrops() {
        return List.copyOf(getTracked().values());
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
