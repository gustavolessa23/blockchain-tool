package com.gustavolessa.blockchain.pool;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.storage.StorageDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class MiningPool {

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

//        if(chain.size() > 0){
//            Block previous = null;
//            while(previous == null){
//                try {
//                    previous = chain.getLastBlock();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            System.err.println("BLOCK PREVIOUS "+previous);
//            t.setPreviousHash(previous.getHash());
//            t.setId(previous.getId()+1);
//        } else {
//            t.setPreviousHash("0");
//            t.setId(1L);
//        }
//        System.out.println("BLOCK POOL: added ID "+t.getId());
        return miningQueue.add(t);
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
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
