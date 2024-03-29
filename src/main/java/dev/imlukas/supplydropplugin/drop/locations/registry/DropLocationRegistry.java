package dev.imlukas.supplydropplugin.drop.locations.registry;

import dev.imlukas.supplydropplugin.drop.locations.DropLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DropLocationRegistry {

    private final HashMap<String, List<DropLocation>> locations = new HashMap<>();

    public DropLocation getFromSerialized(String serialized) {
        DropLocation location = DropLocation.deserialize(serialized);

        for (List<DropLocation> value : locations.values()) {
            for (DropLocation dropLocation : value) {
                if (dropLocation.equals(location)) {
                    return dropLocation;
                }
            }
        }

        return null;
    }

    public void registerLocation(String dropId, DropLocation location) {
        locations.computeIfAbsent(dropId, ignored -> new ArrayList<>()).add(location);
    }

    public DropLocation getNonOccupiedLocation(String dropId) {
        List<DropLocation> dropLocations = this.locations.get(dropId);

        if (dropLocations == null || dropLocations.isEmpty()) {
            return null;
        }

        for (DropLocation location : dropLocations) {
            if (location.isOccupied()) {
                continue;
            }

            return location;
        }

        return null; // All are occupied
    }

    public void clearLocations() {
        locations.clear();
    }
}
