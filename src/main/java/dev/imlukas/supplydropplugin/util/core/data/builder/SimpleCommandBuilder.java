package dev.imlukas.supplydropplugin.util.core.data.builder;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import dev.imlukas.supplydropplugin.util.core.context.CommandArgument;
import dev.imlukas.supplydropplugin.util.core.context.arg.LiteralArgument;
import dev.imlukas.supplydropplugin.util.core.data.Command;
import dev.imlukas.supplydropplugin.util.core.data.CommandBuilder;
import dev.imlukas.supplydropplugin.util.core.data.CommandHandler;
import dev.imlukas.supplydropplugin.util.core.manager.AbstractCommandManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a simple implementation of a CommandBuilder.
 *
 * @param <T> The audience type.
 */
public class SimpleCommandBuilder<T extends CommandAudience> implements CommandBuilder<T> {

    private final List<CommandArgument> arguments;
    private final String name;
    private final Class<T> audienceClass;

    private final AbstractCommandManager manager;

    private CommandHandler<T> handler;
    private String permission;

    public SimpleCommandBuilder(AbstractCommandManager manager, String name, Class<T> audienceClass, List<CommandArgument> arguments, String permission) {
        this.manager = manager;
        this.name = name;
        this.audienceClass = audienceClass;
        this.arguments = arguments;
        this.permission = permission;
    }

    public SimpleCommandBuilder(AbstractCommandManager manager, String name, Class<T> audienceClass) {
        this(
                manager,
                name,
                audienceClass,
                new LinkedList<>(),
                null
        );
    }

    @Override
    public CommandBuilder<T> registerArgument(CommandArgument argument) {
        arguments.add(argument);
        return this;
    }

    @Override
    public CommandBuilder<T> handler(CommandHandler<T> handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public CommandBuilder<T> permission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public <V extends CommandAudience> CommandBuilder<V> audience(Class<V> audience) {
        return new SimpleCommandBuilder<>(manager, name, audience, arguments, permission);
    }

    @Override
    public Command<T> build() {
        List<CommandArgument> realArguments = new LinkedList<>();

        realArguments.add(LiteralArgument.create(name));
        realArguments.addAll(arguments);

        Command<T> command = new SimpleCommand<>(realArguments, handler, audienceClass, permission);
        manager.registerCommand(command);

        return command;
    }
}
