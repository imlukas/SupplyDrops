package dev.imlukas.supplydropplugin.util.file.messages.provider;

import dev.imlukas.supplydropplugin.util.file.messages.Message;
import dev.imlukas.supplydropplugin.util.file.messages.impl.ActionbarMessage;
import dev.imlukas.supplydropplugin.util.file.messages.impl.ChatMessage;
import dev.imlukas.supplydropplugin.util.file.messages.impl.TitleMessage;
import dev.imlukas.supplydropplugin.util.text.ComponentPlaceholder;
import net.kyori.adventure.audience.Audience;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class MessageProviderRegistry {

    private final Map<String, MessageProvider> providers = new HashMap<>();

    public MessageProviderRegistry() {
        registerDefaults();
    }

    public void registerDefaults() {
        register("title", TitleMessage::new);
        register("chat", ChatMessage::new);
        register("actionbar", ActionbarMessage::new);
    }

    public void register(String name, MessageProvider provider) {
        providers.put(name, provider);
    }

    @SafeVarargs
    public final Message get(String name, ConfigurationSection section, ComponentPlaceholder<Audience>... placeholders) {
        return providers.get(name).provide(section, placeholders);
    }
}
