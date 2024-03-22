package dev.imlukas.supplydropplugin.drop.task.drop;

import dev.imlukas.supplydropplugin.drop.Drop;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class DropRepositionTask extends BukkitRunnable {

    private final Drop drop;

    public DropRepositionTask(Drop drop, ArmorStand armorStand) {
        this.drop = drop;
    }

    public Drop getDrop() {
        return drop;
    }

    @Override
    public void run() {
        // TODO: Implement reposition task
    }
}
