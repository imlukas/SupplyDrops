package dev.imlukas.supplydropplugin.util.collection.range;

public interface Range {

    static IntegerRange ofInteger(int min, int max) {
        return new IntegerRange(min, max);
    }

    static LongRange ofLong(long min, long max) {
        return new LongRange(min, max);
    }

    static DoubleRange ofDouble(double min, double max) {
        return new DoubleRange(min, max);
    }
}
