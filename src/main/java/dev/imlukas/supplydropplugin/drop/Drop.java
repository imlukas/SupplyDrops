package dev.imlukas.supplydropplugin.drop;

import dev.imlukas.supplydropplugin.drop.commands.CommandAction;
import dev.imlukas.supplydropplugin.drop.locations.DropLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Drop {

    /**
     * Gets the id of the associated entity of the drop
     * @return the id of the entity
     */
    UUID getEntityId();

    /**
     * @return Drop's unique identifier
     */
    UUID getUUID();

    /**
     * @return the type id of the drop
     */
    String getTypeId();

    /**
     * @return the commands that should be executed when the drop is collected
     */
    CommandAction getCommands();

    /**
     * Drops the drop at specified location
     * @return true if the drop was successfully dropped, false otherwise
     */
    boolean dropAt(DropLocation location);

    /**
     * Drops the drop at a random location, if possible
     * @return true if the drop was successfully dropped, false otherwise
     */
    boolean drop();

    /**
     * Destroys the drop, removing it's associated entity from the world and un-tracking it's location
     */
    void destroy();

    /**
     * Collects the drop, giving the player the rewards
     * @param player the player that collected the drop
     */
    void collect(Player player);
}
