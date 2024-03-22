package dev.imlukas.supplydropplugin.util.file.messages.provider;

import dev.imlukas.supplydropplugin.util.file.messages.Message;
import dev.imlukas.supplydropplugin.util.text.ComponentPlaceholder;
import net.kyori.adventure.audience.Audience;
import org.bukkit.configuration.ConfigurationSection;

public interface MessageProvider {
    Message provide(ConfigurationSection section, ComponentPlaceholder<Audience>... placeholders);
}
