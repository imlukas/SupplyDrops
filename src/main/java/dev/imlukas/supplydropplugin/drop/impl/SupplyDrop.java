package dev.imlukas.supplydropplugin.drop.impl;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.commands.CommandAction;
import dev.imlukas.supplydropplugin.location.SafeLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SupplyDrop implements Drop {

    private final UUID uuid;
    private final CommandAction commands;
    private final SafeLocation dropLocation;

    private UUID entityId;

    public SupplyDrop(UUID uuid, CommandAction commands, SafeLocation dropLocation) {
        this.uuid = uuid;
        this.commands = commands;
        this.dropLocation = dropLocation;
    }

    public static SupplyDrop create(CommandAction commands, SafeLocation dropLocation) {
        return new SupplyDrop(UUID.randomUUID(), commands, dropLocation);
    }

    public static SupplyDrop fromSection(ConfigurationSection section) {
        UUID uuid = UUID.fromString(section.getString("uuid"));
        CommandAction commands = CommandAction.create(section);
        ConfigurationSection locationSection = section.getConfigurationSection("location");
        SafeLocation dropLocation = SafeLocation.fromSection(locationSection);
        return new SupplyDrop(uuid, commands, dropLocation);
    }

    public UUID getId() {
        return uuid;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public CommandAction getCommands() {
        return commands;
    }

    @Override
    public SafeLocation getLocation() {
        return dropLocation;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public void drop() {
        Location dropLocation = this.dropLocation.asBukkitLocation();
        World world = dropLocation.getWorld();

        ArmorStand stand = world.spawn(dropLocation, ArmorStand.class);
        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(stand);

        ActiveModel model = ModelEngineAPI.createActiveModel("airdrop");
        modeledEntity.addModel(model, true);

        setEntityId(stand.getUniqueId());
    }

    @Override
    public void destroy() {
        Entity entity = Bukkit.getEntity(entityId);

        if (entity == null) {
            return;
        }

        entity.remove();
        ModelEngineAPI.removeModeledEntity(entity);
    }

    public void collect(Player player) {
        commands.execute(player);
    }
}
