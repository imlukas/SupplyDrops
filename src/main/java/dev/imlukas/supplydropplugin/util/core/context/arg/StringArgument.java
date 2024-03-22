package dev.imlukas.supplydropplugin.util.core.context.arg;

import dev.imlukas.supplydropplugin.util.core.context.CommandContext;

public class StringArgument extends AbstractArgument<String> {

    public StringArgument(String name) {
        super(name);
    }

    public static StringArgument create(String name) {
        return new StringArgument(name);
    }

    @Override
    public String parse(CommandContext context) {
        return context.getLastInput();
    }

}
