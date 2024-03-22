package dev.imlukas.supplydropplugin.util.file.messages.impl;

import dev.imlukas.supplydropplugin.util.file.messages.Message;
import dev.imlukas.supplydropplugin.util.text.ComponentPlaceholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class RawMessage implements Message {

    private final Component message;
    private final ComponentPlaceholder<Audience>[] placeholders;

    @SafeVarargs
    public RawMessage(String message, ComponentPlaceholder<Audience>... placeholders) {
        this(MiniMessage.miniMessage().deserialize(message), placeholders);
    }

    @SafeVarargs
    public RawMessage(Component message, ComponentPlaceholder<Audience>... placeholders) {
        this.placeholders = placeholders;
        this.message = message;
    }

    @Override
    public Component replacePlaceholders(Component message, Audience audience) {
        for (ComponentPlaceholder<Audience> placeholder : placeholders) {
            message = placeholder.replace(message, audience);
        }

        return message;
    }

    @Override
    public void send(Audience audience) {
        audience.sendMessage(replacePlaceholders(message, audience));
    }
}
