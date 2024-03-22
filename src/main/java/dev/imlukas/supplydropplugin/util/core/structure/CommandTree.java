package dev.imlukas.supplydropplugin.util.core.structure;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import dev.imlukas.supplydropplugin.util.core.context.CommandArgument;
import dev.imlukas.supplydropplugin.util.core.context.CommandContext;
import dev.imlukas.supplydropplugin.util.core.context.impl.MutatingCommandContext;
import dev.imlukas.supplydropplugin.util.core.data.Command;
import dev.imlukas.supplydropplugin.util.core.data.CommandHandler;
import dev.imlukas.supplydropplugin.util.core.manager.AbstractCommandManager;
import dev.imlukas.supplydropplugin.util.core.node.ArgumentCommandNode;
import dev.imlukas.supplydropplugin.util.core.node.CommandNode;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a parsed command tree.
 */
public class CommandTree {

    private final Map<String, CommandNode> roots = new ConcurrentHashMap<>();

    private final AbstractCommandManager manager;

    public CommandTree(AbstractCommandManager manager) {
        this.manager = manager;
    }

    /**
     * Gets the root node for a command.
     *
     * @param name The name of the command.
     * @return The root node.
     */
    public CommandNode getRoot(String name) {
        return roots.get(name);
    }

    /**
     * Gets the target node and context for a command.
     *
     * @param fullInput The full input of the command.
     * @return The target node and context.
     */
    public TargetResult getTargetNode(String fullInput) {
        String[] split = fullInput.split(" ");
        CommandNode node = getRoot(split[0]);

        if (node == null) {
            return null;
        }

        if (fullInput.equalsIgnoreCase(node.getName())) {
            return new TargetResult(node, new MutatingCommandContext(fullInput));
        }

        CommandNode target = node;
        MutatingCommandContext context = new MutatingCommandContext(fullInput);

        boolean valid = false;

        for (int index = 1; index < split.length; index++) {
            String word = split[index];
            List<? extends CommandNode> children = target.getChildren();
            boolean isLast = index == split.length - 1;

            if (children == null) {
                return null;
            }

            CommandNode child = null;

            for (CommandNode nodeChild : children) {
                CommandArgument<?> argument = nodeChild.getArgument();

                if (context.addArgument(word, argument)) {
                    child = nodeChild;

                    if (isLast && child.getChildren().isEmpty()) {
                        valid = true;
                    }
                    break;
                }
            }

            if (child == null) {
                return null;
            }

            target = child;
        }

        if (!valid) {
            return null;
        }

        return new TargetResult(target, context);
    }

    /**
     * Tab completes a command.
     *
     * @param commandAudience The audience to tab complete for.
     * @param commandName     The name of the command.
     * @param split           The argument array.
     * @return The tab completions.
     */
    public List<String> tabComplete(CommandAudience commandAudience, String commandName, String[] split) {
        CommandNode node = getRoot(commandName);
        String fullInput = commandName + " " + String.join(" ", split);

        if (node == null) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();
        CommandNode target = node;

        MutatingCommandContext context = new MutatingCommandContext(fullInput);

        for (int index = 0; index < split.length; index++) {
            String s = split[index];
            List<? extends CommandNode> children = target.getChildren();

            boolean isLast = index == split.length - 1;

            if (children == null) {
                break;
            }

            CommandNode targetChild = null;

            for (CommandNode child : children) {
                if ((!isLast && context.addArgument(s, child.getArgument()))) {
                    targetChild = child;
                    break;
                }

                if (!isLast) {
                    continue;
                }

                CommandArgument<?> argument = child.getArgument();
                String permission = child.getPermission();

                if (permission != null && !commandAudience.hasPermission(child.getPermission())) {
                    continue;
                }

                Class<? extends CommandAudience> targetAudience = child.getTargetAudience();

                if (targetAudience != null && !targetAudience.isAssignableFrom(commandAudience.getClass())) {
                    continue;
                }

                List<String> tabComplete = argument.tabComplete(context);

                if (tabComplete == null) {
                    continue;
                }

                completions.addAll(tabComplete);
            }

            if (targetChild == null) {

                break;
            }

            target = targetChild;
        }

        String lastWord = split[split.length - 1];

        // filter
        if (lastWord.isEmpty()) {
            return completions;
        }

        List<String> filtered = new ArrayList<>();

        for (String completion : completions) {
            if (completion.startsWith(lastWord)) {
                filtered.add(completion);
            }
        }

        return filtered;
    }

    /**
     * Registers a root node to the tree.
     *
     * @param node The node to register.
     */
    public void registerNode(CommandNode node) {
        String name = node.getName();

        if (roots.containsKey(name)) {
            ArgumentCommandNode existing = (ArgumentCommandNode) roots.get(name);
            CommandHandler<?> handler = existing.getCommand().getHandler();

            if (handler == null) {
                migrateChildren(existing, node);
                this.roots.put(name, node);
                return;
            }
            return;
        }

        roots.put(name, node);
        manager.registerRoot(name);
    }

    /**
     * Registers a command to the tree.
     *
     * @param command The command to register.
     */
    public void registerCommand(Command<?> command) {
        List<CommandNode> nodes = new LinkedList<>();

        for (CommandArgument<?> argument : command.getArguments()) {
            CommandNode node = new ArgumentCommandNode(command, argument);
            nodes.add(node);
        }

        // Do children n stuff

        CommandNode root = nodes.get(0);

        registerNode(root);
        root = getRoot(root.getName()); // Get the node from the map. Registering might've ignored it as the node was previously registered

        for (int index = 1; index < nodes.size(); index++) {
            CommandNode node = nodes.get(index);
            root.registerChildren(node);
            root = node;
        }
    }

    private void migrateChildren(CommandNode existing, CommandNode node) {
        List<CommandNode> children = existing.getChildren();

        if (children == null) {
            return;
        }

        for (CommandNode child : children) {
            node.registerChildren(child);
        }
    }

    @Getter
    public static class TargetResult {

        private final CommandNode node;
        private final CommandContext context;

        public TargetResult(CommandNode node, CommandContext context) {
            this.node = node;
            this.context = context;
        }

    }

}
