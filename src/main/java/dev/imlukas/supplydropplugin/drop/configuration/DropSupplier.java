package dev.imlukas.supplydropplugin.drop.configuration;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.drop.Drop;
import dev.imlukas.supplydropplugin.drop.commands.registry.DropCommandRegistry;
import dev.imlukas.supplydropplugin.util.file.YMLBase;

public class DropSupplier extends YMLBase {

    private final DropCommandRegistry commandRegistry;

    public DropSupplier(SupplyDropPlugin plugin) {
        super(plugin, "drops.yml");

        this.commandRegistry = plugin.getCommandRegistry();
    }

    public Drop supplyDrop() {
        return null;
    }
}
