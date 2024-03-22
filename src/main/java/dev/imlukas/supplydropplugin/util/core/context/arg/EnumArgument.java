package dev.imlukas.supplydropplugin.util.core.context.arg;

import dev.imlukas.supplydropplugin.util.core.context.CommandContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnumArgument extends AbstractArgument<Enum<?>> {

    private final Class<? extends Enum<?>> enumClass;

    public EnumArgument(Class<? extends Enum<?>> enumClass, String name) {
        super(name);
        this.enumClass = enumClass;
    }

    public static EnumArgument create(String name, Class<? extends Enum<?>> enumClass) {
        return new EnumArgument(enumClass, name);
    }

    @Override
    public Enum<?> parse(CommandContext context) {
        String input = context.getLastInput();

        for (Enum<?> value : enumClass.getEnumConstants()) {
            if (value.name().equalsIgnoreCase(input)) {
                return value;
            }
        }

        return null;
    }

    @Override
    public List<String> tabComplete(CommandContext context) {
        List<String> completions = new ArrayList<>();

        for (Enum<?> value : enumClass.getEnumConstants()) {
            String name = value.name().toLowerCase(Locale.ROOT);
            completions.add(name);
        }

        return completions;
    }
}
