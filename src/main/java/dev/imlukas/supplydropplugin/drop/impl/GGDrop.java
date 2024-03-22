package dev.imlukas.supplydropplugin.drop.impl;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import dev.imlukas.supplydropplugin.drop.commands.CommandAction;
import dev.imlukas.supplydropplugin.location.SafeLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

import java.util.UUID;

public class GGDrop extends SupplyDrop{

    public GGDrop(UUID uuid, CommandAction commands, SafeLocation dropLocation) {
        super(uuid, commands, dropLocation);
    }

    @Override
    public void drop() {
        Location dropLocation = getLocation().asBukkitLocation();
        World world = dropLocation.getWorld();

        ArmorStand stand = world.spawn(dropLocation, ArmorStand.class);
        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(stand);

        ActiveModel model = ModelEngineAPI.createActiveModel("airdrop_gg");
        modeledEntity.addModel(model, true);
    }
}
