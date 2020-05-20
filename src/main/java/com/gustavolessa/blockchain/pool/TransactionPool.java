package com.gustavolessa.blockchain.pool;

import com.gustavolessa.blockchain.transaction.Transaction;

import javax.enterprise.context.ApplicationScoped;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class TransactionPool extends AbstractQueuePool<Transaction> {


    private final Queue<Transaction> queue = new ConcurrentLinkedQueue<>();

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
}
