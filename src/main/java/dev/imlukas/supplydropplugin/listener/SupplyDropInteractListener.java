package dev.imlukas.supplydropplugin.listener;

import com.ticxo.modelengine.api.entity.BaseEntity;
import com.ticxo.modelengine.api.events.BaseEntityInteractEvent;
import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.cache.DropCache;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.locations.registry.DropLocationRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Queue;
import java.util.UUID;

public class SupplyDropInteractListener implements Listener {

    private final DropCache cache;
    private final DropLocationRegistry locationRegistry;
    private final Queue<Drop> queue;

    public SupplyDropInteractListener(SupplyDropPlugin plugin) {
        this.cache = plugin.getDropCache();
        this.locationRegistry = plugin.getLocationRegistry();
        this.queue = plugin.getDropQueue();
    }

    @EventHandler
    public void onInteract(BaseEntityInteractEvent event) {
        if (event.getAction() != BaseEntityInteractEvent.Action.INTERACT) {
            return;
        }

        Player player = event.getPlayer();
        BaseEntity<?> entity = event.getBaseEntity();

        UUID entityId = entity.getUUID();

        Entity bukkitEntity = Bukkit.getEntity(entityId);

        if (!(bukkitEntity instanceof ArmorStand stand)) {
            return;
        }

        if (!stand.isOnGround()) {
            return;
        }

        Drop drop = cache.getByEntityId(entityId);

        if (drop == null) {
            return;
        }

        drop.destroy();
        drop.collect(player);
        cache.remove(drop.getUUID());
        // Check if there is a drop in the queue that can be dropped after this has been collected
        Drop nextDrop = queue.peek();

        if (nextDrop == null) {
            return;
        }

        boolean dropped = nextDrop.drop();

        if (!dropped) {
            return;
        }

        cache.add(nextDrop);
        queue.poll();
    }
}
