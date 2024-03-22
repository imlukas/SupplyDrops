package dev.imlukas.supplydropplugin.util.core.data;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import dev.imlukas.supplydropplugin.util.core.context.CommandArgument;

import java.util.List;

/**
 * Represents an instance of a  command.
 *
 * @param <T> The type of audience this command will be used for. Audience is the default.
 */
public interface Command<T extends CommandAudience> {

    /**
     * Gets all the arguments for this command.
     *
     * @return The arguments.
     */
    List<CommandArgument> getArguments();

    /**
     * Gets the handler for this command.
     *
     * @return The handler.
     */
    CommandHandler<T> getHandler();

    void setHandler(CommandHandler<T> handler);

    /**
     * Gets the target audience for this command.
     *
     * @return The target audience.
     */
    Class<T> getAudience();

    /**
     * Gets the permission for this command.
     *
     * @return The permission.
     */
    String getPermission();

}
