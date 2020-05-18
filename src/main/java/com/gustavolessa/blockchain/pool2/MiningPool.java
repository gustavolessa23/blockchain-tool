package com.gustavolessa.blockchain.pool2;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.pool.Pool;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class MiningPool implements GenericPool<Block> {

    @Inject
    Blockchain chain;

    private Queue<Block> miningQueue;

    public MiningPool(){
        miningQueue = new ConcurrentLinkedQueue<>();
    }

    public Queue<Block> getMiningQueue() {
        return miningQueue;
    }

    public boolean add(Block t) {
        return miningQueue.add(t);
    }

    @Override
    public Block getFirst() {
        return null;
    }

    @Override
    public List<Block> getMany(int n) {
        return null;
    }

    public Block getFirstBlock() {
        return miningQueue.poll();
    }

    public List<Block> getBlocks(int n) {
        List<Block> list = new ArrayList<>();
        for(int x = 1; x <= n; x++){
            list.add(miningQueue.poll());
        }
        return list;
    }

    public List<Block> getAll() {
        List<Block> tmp = new ArrayList<>(miningQueue);
        this.clear();
        return tmp;
    }

    public boolean clear() {
        miningQueue.clear();
        if (miningQueue.isEmpty()) {
            System.out.println("Block pool cleared!");
            return true;
        } else {
            System.out.println("Could not clear Block pool");
            return false;
        }
    }


    public boolean isEmpty(){
        return miningQueue.isEmpty();
    }

    @Override
    public Block peek() {
        return miningQueue.peek();
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
