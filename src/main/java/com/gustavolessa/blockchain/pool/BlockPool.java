package com.gustavolessa.blockchain.pool;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.storage.StorageDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class BlockPool {

    @Inject
    StorageDAO storage;

    private Queue<Block> queue = new ConcurrentLinkedQueue<>();

    public Queue<Block> getQueue() {
        return queue;
    }
    public boolean add(Block t) {
        System.out.println("POOL: block added "+t);
        storage.saveBlock(t);
        return queue.add(t);
    }

    public Block getFirstBlock() {
        return queue.poll();
    }

    public List<Block> getBlocks(int n) {
        List<Block> list = new ArrayList<>();
        for(int x = 1; x <= n; x++){
            list.add(queue.poll());
        }
        return list;
    }

    public List<Block> getAll() {
        List<Block> tmp = new ArrayList<>(queue);
        this.clear();
        return tmp;
    }

    public boolean clear() {
        queue.clear();
        if (queue.isEmpty()) {
            System.out.println("Block pool cleared!");
            return true;
        } else {
            System.out.println("Could not clear Block pool");
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
