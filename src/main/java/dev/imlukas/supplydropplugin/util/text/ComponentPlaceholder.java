package dev.imlukas.supplydropplugin.util.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.intellij.lang.annotations.RegExp;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ComponentPlaceholder<T> {

    private final Function<T, String> replacer;
    @RegExp
    private String placeholder;

    public ComponentPlaceholder(@RegExp String placeholder, Function<T, String> replacer) {
        this.placeholder = placeholder;
        this.replacer = replacer;
    }

    public ComponentPlaceholder(@RegExp String placeholder, String replacement) {
        this.placeholder = placeholder;
        this.replacer = (t) -> replacement;
    }

    public ComponentPlaceholder(@RegExp String placeholder, CompletableFuture<String> replacement) {
        this.placeholder = placeholder;
        this.replacer = (__) -> replacement.getNow("Loading...");
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public Function<T, String> getReplacer() {
        return replacer;
    }

    public Component replace(Component component, T t) {
        if (!placeholder.startsWith("%")) {
            placeholder = "%" + placeholder;
        }

        if (!placeholder.endsWith("%")) {
            placeholder = placeholder + "%";
        }

        return component.replaceText(TextReplacementConfig.builder().match(placeholder).replacement(replacer.apply(t)).build());
    }

    public Placeholder<T> asRegularPlaceholder() {
        return new Placeholder<>(placeholder, replacer);
    }
}
