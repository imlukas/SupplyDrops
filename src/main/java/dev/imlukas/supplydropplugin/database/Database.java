package dev.imlukas.supplydropplugin.database;

import dev.imlukas.supplydropplugin.database.impl.type.DatabaseType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;

public interface Database {

    DatabaseType getType();

    String getName();


    CompletableFuture<Boolean> enable(JavaPlugin plugin, ConfigurationSection config);

    CompletableFuture<Boolean> enable(ConfigurationSection config);

    CompletableFuture<Void> flush();

    CompletableFuture<Void> wipe();
}
