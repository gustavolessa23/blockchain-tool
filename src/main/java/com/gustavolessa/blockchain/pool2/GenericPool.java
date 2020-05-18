package com.gustavolessa.blockchain.pool2;

import com.gustavolessa.blockchain.transaction.Transaction;

import java.util.List;

public interface GenericPool<T> {
    boolean add(T t);

    T getFirst();

    List<T> getMany(int n);

    List<T> getAll();

    boolean clear();

    boolean isEmpty();

    T peek();
    
}
