package dev.imlukas.supplydropplugin.util.file.messages;

import dev.imlukas.supplydropplugin.util.text.ComponentPlaceholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;

public abstract class AbstractMessage implements Message {

    protected final ComponentPlaceholder<Audience>[] placeholders;
    protected final ConfigurationSection section;

    @SafeVarargs
    protected AbstractMessage(ConfigurationSection section, ComponentPlaceholder<Audience>... placeholders) {
        this.section = section;
        this.placeholders = placeholders;
    }

    public Component replacePlaceholders(Component message, Audience audience) {
        for (ComponentPlaceholder<Audience> placeholder : placeholders) {
            message = placeholder.replace(message, audience);
        }
        return message;
    }
}
