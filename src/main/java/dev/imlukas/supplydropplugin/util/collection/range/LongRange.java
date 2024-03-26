package dev.imlukas.supplydropplugin.util.collection.range;

import java.util.concurrent.ThreadLocalRandom;

public class LongRange extends AbstractRange<Long> {

    public LongRange(Long min, Long max) {
        super(min, max);
    }

    @Override
    public Long random() {
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }
}
