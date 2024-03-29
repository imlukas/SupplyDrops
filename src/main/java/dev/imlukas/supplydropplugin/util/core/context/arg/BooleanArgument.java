package dev.imlukas.supplydropplugin.util.core.context.arg;

import dev.imlukas.supplydropplugin.util.core.context.CommandContext;

public class BooleanArgument extends AbstractArgument<Boolean> {

    public BooleanArgument(String name) {
        super(name);
        tabComplete("true", "false");
    }

    public static BooleanArgument create(String name) {
        return new BooleanArgument(name);
    }

    @Override
    public Boolean parse(CommandContext context) {
        return Boolean.parseBoolean(context.getLastInput());
    }
}
