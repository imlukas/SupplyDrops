package dev.imlukas.supplydropplugin.util.file.messages;

import dev.imlukas.supplydropplugin.util.core.audience.CommandAudience;
import dev.imlukas.supplydropplugin.util.file.YMLBase;
import dev.imlukas.supplydropplugin.util.file.messages.impl.ChatMessage;
import dev.imlukas.supplydropplugin.util.file.messages.impl.RawMessage;
import dev.imlukas.supplydropplugin.util.file.messages.provider.MessageProviderRegistry;
import dev.imlukas.supplydropplugin.util.text.ComponentPlaceholder;
import dev.imlukas.supplydropplugin.util.text.Placeholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

/**
 * This class is responsible for handling the loading and saving of the messages.yml file. It also provides a method for sending messages to players.
 */
public class Messages extends YMLBase {

    private final MessageProviderRegistry registry = new MessageProviderRegistry();

    /**
     * Creates a new messages file.
     * @param plugin The platform this messages file is for.
     */
    public Messages(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "messages.yml"), true);
    }
    public final void announce(String path, List<Placeholder<Audience>> placeholders) {
        Bukkit.getOnlinePlayers().forEach(player -> send(player, path, placeholders.toArray(new Placeholder[0])));
    }
    @SafeVarargs
    public final void announce(String path, Placeholder<Audience>... placeholders) {
        Bukkit.getOnlinePlayers().forEach(player -> send(player, path, placeholders));
    }

    public final void send(CommandAudience audience, String path) {
        if (!getConfiguration().contains(path)) {
            return;
        }

        String msg = getConfiguration().getString(path);

        if (msg == null || msg.isEmpty()) {
            return;
        }

        audience.sendMessage(msg);
    }

    public final void send(Audience sender, String path) {
        send(sender, path, new ComponentPlaceholder[0]);
    }

    @SafeVarargs
    public final void send(Audience sender, String path, Placeholder<Audience>... placeholders) {
        ComponentPlaceholder<Audience>[] componentPlaceholders = new ComponentPlaceholder[placeholders.length];

        for (int i = 0; i < placeholders.length; i++) {
            Placeholder<Audience> placeholder = placeholders[i];
            componentPlaceholders[i] = placeholder.asComponentPlaceholder();
        }

        send(sender, path, componentPlaceholders);
    }

    @SafeVarargs
    public final void send(Audience sender, String path, ComponentPlaceholder<Audience>... placeholders) {
        Message message = getMessage(path, placeholders);

        if (message == null) {
            return;
        }

        message.send(sender);
    }

    @SafeVarargs
    public final RawMessage getRawMessage(String path, ComponentPlaceholder<Audience>... placeholders) {
        String rawMessage = getConfiguration().getString("messages." + path);

        if (rawMessage == null) {
            return null;
        }

        Component message = MiniMessage.miniMessage().deserialize(rawMessage);
        return new RawMessage(message, placeholders);
    }

    @SafeVarargs
    public final Message getMessage(ConfigurationSection section, ComponentPlaceholder<Audience>... placeholders) {
        boolean enabled = section.getBoolean("enabled", true);

        if (!enabled) {
            return null;
        }

        if (!section.contains("type")) {
            return new ChatMessage(section, placeholders);
        }

        String type = section.getString("type");
        return registry.get(type, section, placeholders);
    }

    @SafeVarargs
    public final Message getMessage(String path, ComponentPlaceholder<Audience>... placeholders) {
        ConfigurationSection section = getConfiguration().getConfigurationSection("messages." + path);

        if (section == null) {
            return getRawMessage(path, placeholders);
        }

        return getMessage(section, placeholders);
    }
}
