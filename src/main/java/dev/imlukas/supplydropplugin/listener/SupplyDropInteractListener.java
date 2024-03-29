package dev.imlukas.supplydropplugin.listener;

import com.ticxo.modelengine.api.entity.BaseEntity;
import com.ticxo.modelengine.api.events.BaseEntityInteractEvent;
import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.tracker.DropTracker;
import dev.imlukas.supplydropplugin.drop.Drop;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Queue;
import java.util.UUID;

public class SupplyDropInteractListener implements Listener {

    private final DropTracker dropTracker;
    private final Queue<Drop> queue;

    public SupplyDropInteractListener(SupplyDropPlugin plugin) {
        this.dropTracker = plugin.getDropTracker();
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

        Drop drop = dropTracker.getByEntityId(entityId);

        if (drop == null) {
            return;
        }

        drop.collect(player);
        drop.destroy();
        dropTracker.remove(drop.getUUID());

        // Check if there is a drop in the queue that can be dropped after this has been collected
        Drop nextDrop = queue.peek();

        if (nextDrop == null) {
            return;
        }

        boolean dropped = nextDrop.drop();

        if (!dropped) {
            return;
        }

        dropTracker.add(nextDrop);
        queue.poll();
    }
}
