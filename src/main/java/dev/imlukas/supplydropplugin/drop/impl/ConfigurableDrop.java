package dev.imlukas.supplydropplugin.drop.impl;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.commands.CommandAction;
import dev.imlukas.supplydropplugin.drop.commands.registry.DropCommandRegistry;
import dev.imlukas.supplydropplugin.drop.task.drop.DropRepositionTask;
import dev.imlukas.supplydropplugin.location.DropLocation;
import dev.imlukas.supplydropplugin.util.collection.range.IntegerRange;
import dev.imlukas.supplydropplugin.util.collection.range.Range;
import dev.imlukas.supplydropplugin.util.text.TextUtils;
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

    private final SupplyDropPlugin plugin;
    @Getter(AccessLevel.NONE)
    private final UUID uuid;
    private final String typeId;
    private final String displayName;
    private final CommandAction commands;
    private final String modelId;

    private UUID entityId;
    private Runnable onCollect;

    public ConfigurableDrop(SupplyDropPlugin plugin, UUID uuid, String displayName, CommandAction commands, String typeId, String modelId) {
        this.plugin = plugin;
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

    public static ConfigurableDrop create(SupplyDropPlugin plugin, ConfigurationSection section) {
        UUID uuid = UUID.fromString(section.getString("uuid", UUID.randomUUID().toString()));
        String typeId = section.getName();
        String displayName = section.getString("stand-name");
        String modelId = section.getString("model");

        IntegerRange range = Range.ofInteger(section.getInt("command-range.min"), section.getInt("command-range.max"));
        DropCommandRegistry commandRegistry = plugin.getCommandRegistry();
        CommandAction commandAction = CommandAction.create(commandRegistry.getRandom(section.getName(), range));
        return new ConfigurableDrop(plugin, uuid, displayName, commandAction, typeId, modelId);
    }

    private ArmorStand setupDropAmorStand(Location location) {
        World world = location.getWorld();
        ArmorStand stand = world.spawn(location, ArmorStand.class);
        stand.setInvulnerable(true);
        stand.setVisible(false);
        stand.customName(TextUtils.color(displayName));
        stand.setCustomNameVisible(true);
        new DropRepositionTask(plugin, stand);

        return stand;
    }

    @Override
    public boolean drop() {
        DropLocation dropLocation = plugin.getLocationRegistry().getNonOccupiedLocation(getTypeId());

        if (dropLocation == null) {
            plugin.getDropQueue().add(this);
            return false;
        }

        Location location = dropLocation.asBukkitLocation();
        ArmorStand stand = setupDropAmorStand(location);
        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(stand);
        modeledEntity.getBase().setRenderRadius(1000);
        ActiveModel model = ModelEngineAPI.createActiveModel(modelId);
        modeledEntity.addModel(model, true);

        setEntityId(stand.getUniqueId());
        dropLocation.setOccupied(true);
        this.onCollect = () -> dropLocation.setOccupied(false);
        return true;
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

    @Override
    public void collect(Player player) {

        plugin.getMessages().send(player, "drop.collected");
        plugin.getSounds().playSound(player, "drop.collected");

        onCollect.run();
        commands.execute(player);
    }
}
