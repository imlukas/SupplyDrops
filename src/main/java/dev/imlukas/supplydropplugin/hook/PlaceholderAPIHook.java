package dev.imlukas.supplydropplugin.hook;

import dev.imlukas.supplydropplugin.SupplyDropPlugin;
import dev.imlukas.supplydropplugin.util.time.TimeUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final SupplyDropPlugin plugin;

    public PlaceholderAPIHook(SupplyDropPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "supplydrops";
    }

    @Override
    public @NotNull String getAuthor() {
        return "imlukas";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {

        long seconds = plugin.getDropTask().getTicksRemaining() / 20;

        if (params.contains("hours_remaining")) {
            return String.valueOf(seconds / 3600);
        }

        if (params.contains("minutes_remaining")) {
            return String.valueOf((seconds % 3600) / 60);
        }

        if (params.contains("seconds_remaining")) {
            return String.valueOf(seconds % 60);
        }

        if (params.contains("time_remaining")) {
            return TimeUtil.secondsToTime(seconds);
        }
        return "";
    }
}
