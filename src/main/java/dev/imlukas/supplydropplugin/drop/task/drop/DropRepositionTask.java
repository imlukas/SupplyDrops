package dev.imlukas.supplydropplugin.drop.task.drop;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.Drop;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DropRepositionTask {

    public DropRepositionTask(SupplyDropPlugin plugin, ArmorStand armorStand) {
        Bukkit.getScheduler().runTaskTimer(plugin, (runnable -> {
            armorStand.setVelocity(new Vector(0, plugin.getSettings().getDropVelocity(), 0));

            if (armorStand.isDead()) {
                System.out.println("Armor stand is dead");
                runnable.cancel();
            }
        }), 0, 2);
    }
}
