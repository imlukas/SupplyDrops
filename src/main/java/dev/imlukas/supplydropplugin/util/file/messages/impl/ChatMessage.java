package dev.imlukas.supplydropplugin.util.file.messages.impl;

import dev.imlukas.supplydropplugin.util.file.messages.AbstractMessage;
import dev.imlukas.supplydropplugin.util.text.ComponentPlaceholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ChatMessage extends AbstractMessage {

    private final List<Component> messages = new ArrayList<>();

    @SafeVarargs
    public ChatMessage(ConfigurationSection section, ComponentPlaceholder<Audience>... placeholders) {
        super(section, placeholders);
        MiniMessage miniMessage = MiniMessage.miniMessage();

        List<String> messages = section.getStringList("message");

        if (messages.isEmpty()) {
            this.messages.add(miniMessage.deserialize(section.getString("message")));
            return;
        }

        for (String message : messages) {
            this.messages.add(miniMessage.deserialize(message));
        }
    }

    @Override
    public void send(Audience audience) {
        for (Component message : messages) {
            audience.sendMessage(replacePlaceholders(message, audience));
        }
    }


}
