package dev.imlukas.supplydropplugin.util.core.data;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import dev.imlukas.supplydropplugin.util.core.context.CommandArgument;
import dev.imlukas.supplydropplugin.util.core.context.arg.LiteralArgument;

/**
 * Represents a command builder. This is used to build commands.
 *
 * @param <T> The type of audience this command will be used for. Audience is the default.
 */
public interface CommandBuilder<T extends CommandAudience> {

    /**
     * Registers an argument to the command.
     *
     * @param argument The argument to register.
     * @return The command builder.
     */
    CommandBuilder<T> registerArgument(CommandArgument<?> argument);

    default CommandBuilder<T> registerArgument(String name) {
        return registerArgument(LiteralArgument.create(name));
    }

    /**
     * Sets the handler for the command.
     *
     * @param handler The handler to set.
     * @return The command builder.
     */
    CommandBuilder<T> handler(CommandHandler<T> handler);

    /**
     * Sets the permission for the command.
     *
     * @param permission The permission to set.
     * @return The command builder.
     */
    CommandBuilder<T> permission(String permission);

    /**
     * Sets the new target audience for the command.
     *
     * @param audience The audience to set.
     * @param <V>      The type of audience this command will be used for.
     * @return A new command builder of the new audience type.
     */
    <V extends CommandAudience> CommandBuilder<V> audience(Class<V> audience);

    /**
     * Builds and registers the command.
     *
     * @return The built command.
     */
    Command<T> build();

}
