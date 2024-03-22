package dev.imlukas.supplydropplugin.util.file.messages;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public interface Message {

    Component replacePlaceholders(Component message, Audience audience);

    void send(Audience audience);
}
