package dev.imlukas.supplydropplugin.drop.impl;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.commands.CommandAction;
import dev.imlukas.supplydropplugin.drop.commands.registry.DropCommandRegistry;
import dev.imlukas.supplydropplugin.drop.locations.DropLocation;
import dev.imlukas.supplydropplugin.drop.locations.tracker.DropLocationTracker;
import dev.imlukas.supplydropplugin.drop.messager.DropMessager;
import dev.imlukas.supplydropplugin.drop.task.drop.DropFallTask;
import dev.imlukas.supplydropplugin.util.collection.range.IntegerRange;
import dev.imlukas.supplydropplugin.util.collection.range.Range;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class ConfigurableDrop implements Drop {
    @Getter(AccessLevel.NONE)
    private final SupplyDropPlugin plugin;
    @Getter(AccessLevel.NONE)
    private final DropLocationTracker locationTracker;
    @Getter(AccessLevel.NONE)
    private final UUID uuid;
    private final String typeId;
    private final String displayName;
    private final CommandAction commands;
    private final String modelId;

    private UUID entityId;

    public ConfigurableDrop(SupplyDropPlugin plugin, UUID uuid, String typeId, String displayName, String modelId, CommandAction commands) {
        this.plugin = plugin;
        this.locationTracker = plugin.getLocationTracker();
        this.uuid = uuid;
        this.typeId = typeId;
        this.displayName = displayName;
        this.commands = commands;
        this.modelId = modelId;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    public static ConfigurableDrop create(SupplyDropPlugin plugin, UUID uuid, String typeId, String displayName, String modelId, CommandAction commands) {
        return new ConfigurableDrop(plugin, uuid, typeId, displayName, modelId, commands);
    }

    public static ConfigurableDrop create(SupplyDropPlugin plugin, UUID uuid, ConfigurationSection section) {
        String typeId = section.getName();
        String displayName = section.getString("stand-name");
        String modelId = section.getString("model");

        IntegerRange range = Range.ofInteger(section.getInt("command-range.min"), section.getInt("command-range.max"));
        DropCommandRegistry commandRegistry = plugin.getCommandRegistry();
        CommandAction commandAction = CommandAction.create(commandRegistry.getRandom(section.getName(), range));
        return new ConfigurableDrop(plugin, uuid, typeId, displayName, modelId, commandAction);
    }

    public static ConfigurableDrop create(SupplyDropPlugin plugin, ConfigurationSection section) {
        return create(plugin, UUID.randomUUID(), section);
    }

    private ArmorStand setupDropAmorStand(Location location) {
        World world = location.getWorld();
        ArmorStand stand = world.spawn(location, ArmorStand.class);
        stand.setInvulnerable(true);
        stand.setVisible(false);
        stand.setCustomNameVisible(true);
        new DropFallTask(plugin, displayName, stand);
        return stand;
    }

    @Override
    public boolean dropAt(DropLocation dropLocation) {
        Location location = dropLocation.asBukkitLocation();
        locationTracker.track(this, dropLocation);

        ArmorStand stand = setupDropAmorStand(location);
        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(stand);
        modeledEntity.getBase().setRenderRadius(1000);
        ActiveModel model = ModelEngineAPI.createActiveModel(modelId);
        modeledEntity.addModel(model, true);

        setEntityId(stand.getUniqueId());
        dropLocation.setOccupied(true);

        DropMessager.announceDropping(plugin, this, dropLocation);
        return true;
    }

    @Override
    public boolean drop() {
        DropLocation dropLocation = plugin.getLocationRegistry().getNonOccupiedLocation(getTypeId());

        if (dropLocation == null) {
            plugin.getDropQueue().add(this);
            return false;
        }

        return dropAt(dropLocation);
    }

    @Override
    public void destroy() {
        Entity entity = Bukkit.getEntity(entityId);

        if (entity == null) {
            return;
        }

        entity.remove();
        ModelEngineAPI.removeModeledEntity(entity);
        locationTracker.untrack(this);
    }

    @Override
    public void collect(Player player) {
        DropMessager.sendCollectNotifications(plugin, player, this);
        DropMessager.announceCollected(plugin, player, this, locationTracker.get(this));

        commands.execute(player);
    }
}
