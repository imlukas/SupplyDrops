package dev.imlukas.supplydropplugin.drop.locations.tracker;

import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.locations.DropLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DropLocationTracker {

    private final Map<UUID, DropLocation> locations = new HashMap<>();

    public void track(Drop drop, DropLocation location) {
        locations.put(drop.getUUID(), location);
    }

    public void untrack(Drop drop) {
        DropLocation location = locations.remove(drop.getUUID());
        if (location != null) {
            location.setOccupied(false);
        }
    }

    public DropLocation get(Drop drop) {
        return locations.get(drop.getUUID());
    }

    public boolean isTracked(UUID dropId) {
        return locations.containsKey(dropId);
    }

    public Map<UUID, DropLocation> getTracked() {
        return locations;
    }
}
