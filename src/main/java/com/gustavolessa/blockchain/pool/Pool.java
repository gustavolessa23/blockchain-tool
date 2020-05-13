package com.gustavolessa.blockchain.pool;

import com.gustavolessa.blockchain.transaction.Transaction;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface Pool {
    boolean add(Transaction t);

    Transaction getFirstTransaction();

    List<Transaction> getTransactions(int n);

    List<Transaction> getAll();

    boolean clear();
    
}
