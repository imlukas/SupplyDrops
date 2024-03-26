package dev.imlukas.supplydropplugin.util.collection;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

public class PredicateQueue<E> extends LinkedList<E> {

    /**
     * Retrieves but does not remove the first element to satisfy the predicate.
     * @param predicate The predicate to satisfy.
     * @return The first element to satisfy the predicate, or null if no element satisfies the predicate.
     */
    public E peekIf(Predicate<E> predicate) {
        for (E element : this) {
            if (predicate.test(element)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Retrieves and removes the first element to satisfy the predicate.
     * @param predicate The predicate to satisfy.
     * @return The first element to satisfy the predicate, or null if no element satisfies the predicate.
     */
    public E pollIf(Predicate<E> predicate) {
        for (E element : this) {
            if (predicate.test(element)) {
                remove(element);
                return element;
            }
        }
        return null;
    }
}
