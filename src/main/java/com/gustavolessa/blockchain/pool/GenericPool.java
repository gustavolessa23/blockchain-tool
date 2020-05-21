package com.gustavolessa.blockchain.pool;

import java.util.List;

/**
 * Generic interface to create a pool of any object using any data structure.
 * @param <T>
 */
public interface GenericPool<T> {
    boolean add(T t);

    T getFirst();

    List<T> getMany(int n);

    List<T> getAll();

    List<T> readMany(int n);

    List<T> readAll();

    boolean clear();

    boolean isEmpty();

    T readFirst();

}
