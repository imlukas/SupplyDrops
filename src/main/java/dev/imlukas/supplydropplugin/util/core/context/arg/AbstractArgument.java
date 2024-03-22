package dev.imlukas.supplydropplugin.util.core.context.arg;

import dev.imlukas.supplydropplugin.util.core.context.CommandArgument;
import dev.imlukas.supplydropplugin.util.core.context.CommandContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractArgument<T> implements CommandArgument<T> {

    private final String name;
    private final List<String> tabComplete = new ArrayList<>();

    protected AbstractArgument(String name) {
        this.name = name;
    }

    public AbstractArgument<T> tabComplete(String... tabComplete) {
        this.tabComplete.addAll(Arrays.asList(tabComplete));
        return this;
    }

    public AbstractArgument<T> tabComplete(List<String> tabComplete) {
        this.tabComplete.addAll(tabComplete);
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> tabComplete(CommandContext context) {
        return tabComplete;
    }
}
