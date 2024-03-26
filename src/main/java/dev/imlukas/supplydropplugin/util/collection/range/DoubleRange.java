package dev.imlukas.supplydropplugin.util.collection.range;

import java.util.concurrent.ThreadLocalRandom;

public class DoubleRange extends AbstractRange<Double> {

    public DoubleRange(double min, double max) {
        super(min, max);
    }

    @Override
    public Double random() {
        return ThreadLocalRandom.current().nextDouble(min, max + 1);
    }
}
