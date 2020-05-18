package com.gustavolessa.blockchain.pool2;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.block.Block;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class TransmissionPool {

    private Queue<Block> queue = new ConcurrentLinkedQueue<>();

    public Queue<Block> getQueue() {
        return queue;
    }
    public boolean add(Block t) {
        System.out.println("TRANSMISSION POOL: block added "+t.getId());
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
            System.out.println("Transmission pool cleared!");
            return true;
        } else {
            System.out.println("Could not clear transmission pool");
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
