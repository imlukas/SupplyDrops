package dev.imlukas.supplydropplugin.util.time;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * Represents a time value.
 */
public class Time {

    private final long time;
    private final TimeUnit unit;

    public Time(long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }

    public static Time fromString(String time) {
        return TimeParser.parse(time);
    }

    public static Time sinceEpoch(long epoch) {
        long currentEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return new Time((int) (currentEpoch - epoch), TimeUnit.SECONDS);
    }

    /**
     * Converts the time to the specified unit.
     *
     * @param unit The unit to convert to
     * @return The converted time
     */
    public long as(TimeUnit unit) {
        return unit.convert(time, this.unit);
    }

    /**
     * Converts the time to ticks.
     *
     * @return The converted time
     */
    public long asTicks() {
        return as(TimeUnit.MILLISECONDS) / 50;
    }

    public Time add(Time time) {
        return new Time((int) (as(TimeUnit.MILLISECONDS) + time.as(TimeUnit.MILLISECONDS)), TimeUnit.MILLISECONDS);
    }

    public Time subtract(Time time) {
        return new Time((int) (as(TimeUnit.MILLISECONDS) - time.as(TimeUnit.MILLISECONDS)), TimeUnit.MILLISECONDS);
    }


    @Override
    public String toString() {
        return time + " " + unit.name().toLowerCase();
    }
}
