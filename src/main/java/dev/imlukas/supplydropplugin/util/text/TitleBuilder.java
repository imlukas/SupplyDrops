package dev.imlukas.supplydropplugin.util.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;

import java.time.Duration;

public class TitleBuilder {


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Component title;
        private Component subtitle;
        private Duration fadeIn = getDuration(5);
        private Duration stay = getDuration(5);
        private Duration fadeOut = getDuration(5);


        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder subtitle(Component subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder title(String title) {
            this.title = MiniMessage.miniMessage().deserialize(title);
            return this;
        }

        public Builder subtitle(String subtitle) {
            this.subtitle = MiniMessage.miniMessage().deserialize(subtitle);
            return this;
        }

        public Builder fadeIn(int fadeIn) {
            this.fadeIn = getDuration(fadeIn);
            return this;
        }

        public Builder stay(int stay) {
            this.stay = getDuration(stay);
            return this;
        }

        public Builder fadeOut(int fadeOut) {
            this.fadeOut = getDuration(fadeOut);
            return this;
        }

        public Builder times(int fadeIn, int stay, int fadeOut) {
            this.fadeIn = getDuration(fadeIn);
            this.stay = getDuration(stay);
            this.fadeOut = getDuration(fadeOut);
            return this;
        }

        public Title build() {
            return Title.title(title, subtitle, Title.Times.times(fadeIn, stay, fadeOut));
        }

        private Duration getDuration(long ticks) {
            return Duration.ofMillis(ticks * 50);
        }
    }
}
