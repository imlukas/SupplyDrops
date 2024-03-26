package dev.imlukas.supplydropplugin.listener;

import com.ticxo.modelengine.api.entity.BaseEntity;
import com.ticxo.modelengine.api.events.BaseEntityInteractEvent;
import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.cache.DropCache;
import dev.imlukas.supplydropplugin.drop.Drop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class SupplyDropInteractListener implements Listener {

    private final DropCache cache;

    public SupplyDropInteractListener(SupplyDropPlugin plugin) {
        this.cache = plugin.getDropCache();
    }

    @EventHandler
    public void onInteract(BaseEntityInteractEvent event) {
        if (event.getAction() != BaseEntityInteractEvent.Action.INTERACT) {
            return;
        }

        Player player = event.getPlayer();
        BaseEntity<?> entity = event.getBaseEntity();

        UUID entityId = entity.getUUID();
        Drop drop = cache.getByEntityId(entityId);

        if (drop == null) {
            return;
        }

        drop.destroy();
        drop.collect(player);
        cache.remove(drop.getId());
    }
}
