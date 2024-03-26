package dev.imlukas.supplydropplugin.util.collection.range;

public abstract class AbstractRange<F extends Number> {

    protected final F min;
    protected final F max;

    protected AbstractRange(F min, F max) {
        this.min = min;
        this.max = max;
    }

    public abstract F random();

    public F min() {
        return min;
    }

    public F max() {
        return max;
    }
}
