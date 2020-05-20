package com.gustavolessa.blockchain.pool;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractQueuePool<T> implements GenericPool<T> {

    Queue<T> pool;

    public AbstractQueuePool() {
        pool = new ConcurrentLinkedQueue<>();
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    @Override
    public boolean add(T block) {
        return pool.add(block);
    }

    @Override
    public T getFirst() {
        return pool.poll();
    }

    @Override
    public List<T> getMany(int n) {
        List<T> list = new ArrayList<>();
        for (int x = 1; x <= n; x++) {
            list.add(pool.poll());
        }
        return list;
    }

    @Override
    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        while (!pool.isEmpty()) {
            list.add(pool.poll());
        }
        return list;
    }

    @Override
    public List<T> readMany(int n) {
        List<T> tmp = new ArrayList<>(pool);
        Collections.reverse(tmp);
        return tmp.subList(0, n - 1);
    }

    @Override
    public List<T> readAll() {
        List<T> tmp = new ArrayList<>(pool);
        Collections.reverse(tmp);
        return tmp;
    }

    @Override
    public boolean clear() {
        pool.clear();
        if (pool.isEmpty()) {
            System.out.println("Block pool cleared!");
            return true;
        } else {
            System.out.println("Could not clear Block pool");
            return false;
        }
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public T readFirst() {
        return pool.peek();
    }
}
