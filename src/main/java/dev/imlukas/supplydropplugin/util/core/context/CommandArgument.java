package dev.imlukas.supplydropplugin.util.core.context;

import dev.imlukas.supplydropplugin.util.core.context.arg.ProxiedDefaultArgument;

import java.util.Collections;
import java.util.List;

/**
 * Represents a command argument. This will then be parsed by the command context.
 */
public interface CommandArgument<T> {

    /**
     * Gets the name of this argument. This is used to retrieve the argument from the command context.
     *
     * @return The name of this argument.
     */
    String getName();

    /**
     * Parses the argument from the command context into an object.
     *
     * @param context The command context.
     * @return The parsed object.
     */
    T parse(CommandContext context);

    /**
     * Gets the tab completion for this argument.
     *
     * @param context The command context.
     * @return The tab completion.
     */
    default List<String> tabComplete(CommandContext context) {
        return Collections.emptyList();
    }

    default CommandArgument<?> orDefault(Object defaultValue) {
        return new ProxiedDefaultArgument(this, defaultValue);
    }

}
