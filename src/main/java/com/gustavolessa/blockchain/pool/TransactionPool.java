package com.gustavolessa.blockchain.pool;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.transaction.Transaction;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class TransactionPool extends AbstractQueuePool<Transaction> {


    private Queue<Transaction> queue = new ConcurrentLinkedQueue<>();


//    @Override
//    public boolean add(Transaction t) {
//        System.out.println("Transaction added "+t.getHash());
//        return queue.add(t);
//    }
//
//    @Override
//    public Transaction getFirst() {
//        return queue.poll();
//    }
//
//    @Override
//    public List<Transaction> getMany(int n) {
//        List<Transaction> list = new ArrayList<>();
//        for(int x = 1; x <= n; x++){
//            list.add(queue.poll());
//        }
//        return list;
//    }
//
//    @Override
//    public List<Transaction> getAll() {
//        List<Transaction> tmp = new ArrayList<>(queue);
//        this.clear();
//        return tmp;
//    }

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


//    public boolean isEmpty(){
//        return queue.isEmpty();
//    }
//
//    @Override
//    public Transaction readFirst() {
//        return queue.peek();
//    }
//
//    @Override
//    public String toString() {
//        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
//    }
}
