package dev.imlukas.supplydropplugin.util.core.context.arg;

import dev.imlukas.supplydropplugin.util.core.context.CommandContext;

public class IntegerArgument extends AbstractArgument<Integer> {

    public IntegerArgument(String name) {
        super(name);
    }

    public static IntegerArgument create(String name, String... tabComplete) {
        IntegerArgument integerArgument = create(name);
        integerArgument.tabComplete(tabComplete);

        return integerArgument;
    }

    public static IntegerArgument create(String name) {
        return new IntegerArgument(name);
    }

    @Override
    public Integer parse(CommandContext context) {
        try {
            return Integer.parseInt(context.getLastInput());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
