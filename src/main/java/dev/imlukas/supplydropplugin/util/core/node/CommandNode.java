package dev.imlukas.supplydropplugin.util.core.node;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import dev.imlukas.supplydropplugin.util.core.context.CommandArgument;
import dev.imlukas.supplydropplugin.util.core.context.arg.LiteralArgument;

import java.util.List;

/**
 * Represents a command node. This is used to build the command tree.
 */
public interface CommandNode {

    /**
     * Gets the name of this node.
     *
     * @return The name.
     */
    String getName();

    /**
     * Gets the permission for this node.
     *
     * @return The permission.
     */
    String getPermission();

    /**
     * Gets the children of this node.
     *
     * @return The children.
     */
    List<CommandNode> getChildren();

    /**
     * Gets the parent of this node. Null if this is a root node.
     *
     * @return The parent.
     */
    CommandNode getParent();

    /**
     * Sets the parent of this node.
     *
     * @param node The parent.
     */
    void setParent(CommandNode node);

    /**
     * Registers children to this node.
     *
     * @param nodes The children.
     */
    void registerChildren(CommandNode... nodes);

    /**
     * Removes children from this node.
     *
     * @param nodes The children.
     */
    void removeChildren(CommandNode... nodes);

    /**
     * Gets the argument for this node.
     *
     * @return The argument.
     */
    default CommandArgument getArgument() {
        return new LiteralArgument(getName());
    }

    /**
     * Gets the target audience for this node.
     *
     * @return The target audience.
     */
    Class<? extends CommandAudience> getTargetAudience();

}
