package dev.imlukas.supplydropplugin.util.core.manager;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import dev.imlukas.supplydropplugin.util.core.context.CommandContext;
import dev.imlukas.supplydropplugin.util.core.data.Command;
import dev.imlukas.supplydropplugin.util.core.data.CommandBuilder;
import dev.imlukas.supplydropplugin.util.core.data.builder.SimpleCommandBuilder;
import dev.imlukas.supplydropplugin.util.core.node.ArgumentCommandNode;
import dev.imlukas.supplydropplugin.util.core.node.CommandNode;
import dev.imlukas.supplydropplugin.util.core.structure.CommandTree;
import dev.imlukas.supplydropplugin.util.core.structure.CommandTree.TargetResult;
import dev.imlukas.supplydropplugin.util.file.messages.Messages;

import java.util.List;

/**
 * Represents an abstract implementation of a CommandManager. This implementation provides the command tree logic, and the command handling logic.
 */
public abstract class AbstractCommandManager implements CommandManager<CommandAudience> {

    protected final Messages messages;
    protected final CommandTree commandTree = new CommandTree(this);

    public AbstractCommandManager(Messages messages) {
        this.messages = messages;
    }

    @Override
    public CommandBuilder<CommandAudience> newCommand(String name) {
        return new SimpleCommandBuilder<>(this, name, CommandAudience.class);
    }

    public void registerCommand(Command<?> command) {
        commandTree.registerCommand(command);
    }

    private String createInput(String label, String[] args) {
        return (args.length == 0) ? label : label + " " + String.join(" ", args);
    }

    protected void handle(CommandAudience commandAudience, String label, String[] args) {
        handle(commandAudience, createInput(label, args));
    }

    protected <T extends CommandAudience> void handle(CommandAudience commandAudience, String input) {
        TargetResult result = commandTree.getTargetNode(input);

        if (result == null) {
            messages.send(commandAudience, "command.invalid");
            return;
        }

        CommandNode node = result.getNode();

        if (!(node instanceof ArgumentCommandNode argumentNode)) {
            return;
        }

        CommandContext context = result.getContext();
        Command<T> command = (Command<T>) argumentNode.getCommand();

        Class<T> audienceClass = command.getAudience();

        String permission = command.getPermission();
        boolean hasPermission = permission == null || commandAudience.hasPermission(permission);

        if (!hasPermission) {
            messages.send(commandAudience, "command.no-permission");
            return;
        }

        if (!audienceClass.isAssignableFrom(commandAudience.getClass())) {
            messages.send(commandAudience, "command.incorrect-audience");
            return;
        }

        command.getHandler().handle((T) commandAudience, context);
    }

    protected List<String> tabComplete(CommandAudience commandAudience, String label, String[] args) {
        return commandTree.tabComplete(commandAudience, label, args);
    }

    public abstract void registerRoot(String name);

}
