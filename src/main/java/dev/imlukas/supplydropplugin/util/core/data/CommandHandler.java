package dev.imlukas.supplydropplugin.util.core.data;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import dev.imlukas.supplydropplugin.util.core.context.CommandContext;

/**
 * Represents a command handler. This is the lambda that is called when a command is executed.
 *
 * @param <T> The type of audience this command handler will be used for. Audience is the default.
 */
public interface CommandHandler<T extends CommandAudience> {

    void handle(T audience, CommandContext context);

}
