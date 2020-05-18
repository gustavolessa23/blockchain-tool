package com.gustavolessa.blockchain;

import com.google.common.collect.Lists;
import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.block.BlockHelper;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import com.gustavolessa.blockchain.storage.StorageDAO;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Consumer class
 */
@ApplicationScoped
public class Miner implements Runnable {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Inject
    MiningPool miningPool;

    @Inject
    Blockchain chain;

    @Inject
    StorageDAO storage;

    @Inject
    TransmissionPool transmissionPool;

    //private volatile Block b;

    void onStart(@Observes StartupEvent ev) {
 //       scheduler.submit(this);
        scheduler.scheduleWithFixedDelay(this, 1L, 1L, TimeUnit.SECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        if(!miningPool.isEmpty()){
            Block blockToMine = miningPool.getFirst();
            long previousId = 0L;
            String previousHash = "0";

            try {
                previousHash = chain.getLastBlock().getHash();
                previousId = chain.getLastBlock().getId();
            } catch (Exception e) {
                System.err.println("This is the genesis block!!!");
            }
            blockToMine.setId(previousId+1);
            blockToMine.setPreviousHash(previousHash);

//            synchronized (b){
            //    try{
            System.err.println("Mining block...");
            blockToMine.mine(Main.BlockchainTool.difficulty);

            //}

            List<Block> tmp = Lists.newArrayList(chain.getMined());
            tmp.add(blockToMine);
          //  System.out.println("TEMP CHAIN "+tmp);
            if(BlockHelper.isChainValid(tmp, Main.BlockchainTool.difficulty)){
                System.err.println("BLOCK VALID: "+blockToMine.getId());
                chain.add(blockToMine);
                storage.saveBlock(blockToMine);
                transmissionPool.add(blockToMine);
            } else {
                System.err.println("Block invalid for the chain.");
            }
        }
    }
}