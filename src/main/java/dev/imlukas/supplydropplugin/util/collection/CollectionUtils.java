package dev.imlukas.supplydropplugin.util.collection;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class CollectionUtils {

    private CollectionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T getRandom(Collection<T> set) {
        int size = set.size();

        if (size == 0) {
            return null;
        }

        int index = ThreadLocalRandom.current().nextInt(size);
        int i = 0;

        for (T t : set) {
            if (i == index) {
                return t;
            }

            i++;
        }

        return null;
    }

    public static <T> T getRandom(List<T> list) {
        int size = list.size();

        if (size == 0) {
            return null;
        }

        if (size == 1) {
            return list.get(0);
        }

        Random random = ThreadLocalRandom.current();

        return list.get(random.nextInt(size));
    }

    public static <T> T getRandom(T[] array) {
        int size = array.length;

        if (size == 0) {
            return null;
        }

        if (size == 1) {
            return array[0];
        }

        Random random = ThreadLocalRandom.current();

        return array[random.nextInt(size)];
    }

    public static <T> List<T> randomize(List<T> list) {
        Collections.shuffle(list);
        return list;
    }

    public static <T> void insertInList(List<T> list, int index, T value) {
        if (index >= list.size()) {
            list.add(value);
            return;
        }

        List<T> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == index) {
                newList.add(value);
            }

            newList.add(list.get(i));
        }

        list.clear();
        list.addAll(newList);
    }
}
