package dev.imlukas.supplydropplugin.util.time;

import java.sql.Struct;

public class TimeUtil {

    public static String secondsToTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
