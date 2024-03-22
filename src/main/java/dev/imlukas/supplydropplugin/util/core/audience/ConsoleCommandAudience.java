package dev.imlukas.supplydropplugin.util.core.audience;

/**
 * Represents a console audience. This allows us to filter console-only commands.
 */
public abstract class ConsoleCommandAudience implements CommandAudience {

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public boolean isConsole() {
        return true;
    }
}
