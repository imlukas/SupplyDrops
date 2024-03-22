package dev.imlukas.supplydropplugin.util.file.messages.impl;

import dev.imlukas.supplydropplugin.util.file.messages.AbstractMessage;
import dev.imlukas.supplydropplugin.util.text.ComponentPlaceholder;
import dev.imlukas.supplydropplugin.util.text.TitleBuilder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;

public class TitleMessage extends AbstractMessage {

    private final Component title;
    private final Component subtitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    @SafeVarargs
    public TitleMessage(ConfigurationSection section, ComponentPlaceholder<Audience>... placeholders) {
        super(section, placeholders);
        title = MiniMessage.miniMessage().deserialize(section.getString("title", ""));
        subtitle = MiniMessage.miniMessage().deserialize(section.getString("subtitle", ""));
        fadeIn = section.getInt("fadeIn", 60);
        stay = section.getInt("stay", 20);
        fadeOut = section.getInt("fadeOut", 60);
    }

    @Override
    public void send(Audience audience) {
        Component title = replacePlaceholders(this.title, audience);
        Component subtitle = replacePlaceholders(this.subtitle, audience);

        audience.showTitle(TitleBuilder.builder()
                .title(title)
                .subtitle(subtitle)
                .times(fadeIn, stay, fadeOut)
                .build());
    }
}
