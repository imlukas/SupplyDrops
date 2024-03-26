package dev.imlukas.supplydropplugin.util.collection.range;

import java.util.concurrent.ThreadLocalRandom;

public class IntegerRange extends AbstractRange<Integer> {

    public IntegerRange(Integer min, Integer max) {
        super(min, max);
    }

    @Override
    public Integer random() {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
