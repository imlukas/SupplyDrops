package dev.imlukas.supplydropplugin.util.core.manager;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import dev.imlukas.supplydropplugin.util.core.data.CommandBuilder;

/**
 * Represents the proprietary command manager for Core. This is used to register commands across all platforms.
 *
 * @param <T> The type of audience this command manager will be used for. Audience is the default.
 */
public interface CommandManager<T extends CommandAudience> {

    /**
     * Creates a new command builder.
     *
     * @param name The name of the command. This is the first word after the slash.
     * @return The command builder.
     */
    CommandBuilder<T> newCommand(String name);

    void syncCommands();


}
