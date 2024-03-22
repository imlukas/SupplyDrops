package dev.imlukas.supplydropplugin.util.core.context.arg;

import dev.imlukas.supplydropplugin.util.core.context.CommandArgument;
import dev.imlukas.supplydropplugin.util.core.context.CommandContext;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ListArgument<T> implements CommandArgument<Object> {

    private final String name;
    private final Supplier<Collection<T>> objects;
    private final Function<T, String> toString;
    private final Function<String, T> fromString;

    public ListArgument(String name, Supplier<Collection<T>> objects, Function<T, String> toString, Function<String, T> fromString) {
        this.name = name;
        this.objects = objects;
        this.toString = toString;
        this.fromString = fromString;
    }

    public static <T> ListArgument<T> create(String name, Collection<T> objects, Function<T, String> toString, Function<String, T> fromString) {
        return new ListArgument<>(name, () -> objects, toString, fromString);
    }

    public static ListArgument<String> create(String name, Collection<String> objects) {
        return create(name, objects, Function.identity(), Function.identity());
    }

    public static <T> ListArgument<T> create(String name, Collection<T> objects, Function<T, String> toString) {
        return create(name, objects, toString, (text) -> {
            for (T object : objects) {
                if (toString.apply(object).equalsIgnoreCase(text)) {
                    return object;
                }
            }

            return null;
        });
    }

    public static <T> ListArgument<T> create(String name, Supplier<Collection<T>> objects, Function<T, String> toString, Function<String, T> fromString) {
        return new ListArgument<>(name, objects, toString, fromString);
    }

    public static ListArgument<String> create(String name, Supplier<Collection<String>> objects) {
        return create(name, objects, Function.identity(), Function.identity());
    }

    public static <T> ListArgument<T> create(String name, Supplier<Collection<T>> objects, Function<T, String> toString) {
        return create(name, objects, toString, (text) -> {
            for (T object : objects.get()) {
                if (toString.apply(object).equalsIgnoreCase(text)) {
                    return object;
                }
            }

            return null;
        });
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object parse(CommandContext context) {
        return fromString.apply(context.getLastInput());
    }

    @Override
    public List<String> tabComplete(CommandContext context) {
        List<String> completions = new LinkedList<>();

        for (T object : objects.get()) {
            completions.add(toString.apply(object));
        }

        return completions;
    }
}
