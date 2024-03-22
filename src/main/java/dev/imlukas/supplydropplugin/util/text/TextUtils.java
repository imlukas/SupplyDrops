package dev.imlukas.supplydropplugin.util.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a utility class for text.
 */
public final class TextUtils {

    private static final Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]){6}");

    private TextUtils() {
    }

    /**
     * Deserializes a String to a Component using MiniMessage.
     *
     * @param message The String to deserialize
     * @return The deserialized Component
     */
    public static Component color(String message) {
        return MiniMessage.miniMessage().deserialize(message).decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> color(List<String> messages) {
        List<Component> colored = new ArrayList<>();

        for (String message : messages) {
            colored.add(color(message));
        }

        return colored;
    }

    /**
     * Colors a List of Strings, replacing all hex colors with their respective ChatColor.
     *
     * @param messages The List of Strings to color
     * @return The colored List of Strings
     */
    public static List<String> legacyColor(List<String> messages) {
        List<String> colored = new ArrayList<>();

        for (String message : messages) {
            colored.add(legacyColor(message));
        }

        return colored;
    }

    /**
     * Colors a String, replacing all hex colors with their respective ChatColor.
     *
     * @param text The String to color
     * @return The colored String
     */
    public static String legacyColor(String text) {
        Matcher matcher = hexPattern.matcher(text);
        while (matcher.find()) {
            String color = text.substring(matcher.start(), matcher.end());
            text = text.replace(color, String.valueOf(ChatColor.of(color)));
            matcher = hexPattern.matcher(text);
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Capitalizes a String. (e.g. "hello" -> "Hello")
     *
     * @param toCapitalize The String to capitalize
     * @return The capitalized String
     */
    public static String capitalize(String toCapitalize) {
        return toCapitalize.substring(0, 1).toUpperCase() + toCapitalize.substring(1);
    }

    /**
     * Formats an Enum to a readable String. (e.g. "EASY" -> "Easy")
     *
     * @param enumToText The Enum to format
     * @return The formatted String
     */
    public static String formatEnum(Enum<?> enumToText) {
        return capitalize(enumToText.toString().replace("_", " "));
    }

    /**
     * Formats an Array of Enums to readable Strings. (e.g. ["EASY", "HARD"] -> ["Easy", "Hard"])
     *
     * @param enumToText The List of Enums to format
     * @return The formatted List of Strings
     */
    public static List<String> formatEnum(Enum<?>[] enumToText) {
        List<String> formatted = new ArrayList<>();

        for (Enum<?> string : enumToText) {
            formatted.add(formatEnum(string));
        }

        return formatted;
    }

    /**
     * Removes a file extension from a String (e.g. "hello.txt" -> "hello")
     *
     * @param fileName The String to remove the file extension from
     * @return The String without the file extension
     */
    public static String removeFileExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
}

