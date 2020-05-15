package com.gustavolessa.blockchain.pool;

import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.gustavolessa.blockchain.transaction.Transaction;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class TransactionPool implements Pool {


    private Queue<Transaction> queue = new ConcurrentLinkedQueue<>();

    public Queue<Transaction> getQueue() {
        return queue;
    }
    @Override
    public boolean add(Transaction t) {
        System.out.println("POOL: transaction added "+t);
        return queue.add(t);
    }

    @Override
    public Transaction getFirstTransaction() {
        return queue.poll();
    }

    @Override
    public List<Transaction> getTransactions(int n) {
        List<Transaction> list = new ArrayList<>();
        for(int x = 1; x <= n; x++){
            list.add(queue.poll());
        }
        return list;
    }

    @Override
    public List<Transaction> getAll() {
        List<Transaction> tmp = new ArrayList<>(queue);
        this.clear();
        return tmp;
    }

    @Override
    public boolean clear() {
        queue.clear();
        if (queue.isEmpty()) {
            System.out.println("Transactions pool cleared!");
            return true;
        } else {
            System.out.println("Could not clear transactions pool");
            return false;
        }
    }


    public boolean isEmpty(){
        return queue.isEmpty();
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
