package com.gustavolessa.blockchain.block;

import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.chain.BlockchainHelper;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import com.gustavolessa.blockchain.services.application.Runner;
import com.gustavolessa.blockchain.storage.StorageDAO;
import io.quarkus.runtime.ShutdownEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Consumer class
 */
@ApplicationScoped
public class Miner implements Runnable {

    private ScheduledExecutorService scheduler;

    @Inject
    MiningPool miningPool;

    @Inject
    Blockchain chain;

    @Inject
    StorageDAO storage;

    @Inject
    TransmissionPool transmissionPool;

    @Inject
    TransactionPool transactionPool;

    public Miner() {
        this.resetExecutor();
    }

    public void resetExecutor() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void startMining() {
        scheduler.shutdown();
        this.resetExecutor();
        System.out.println("Starting to mine new blocks...");
        scheduler.scheduleWithFixedDelay(this, 0, 3L, TimeUnit.SECONDS);
    }

    public void stopMining() {
        System.out.println("Stopping to mine new blocks...");
        scheduler.shutdown();
        //  this.resetExecutor();
    }

    public void flipSwitch() {
        if (scheduler.isShutdown()) {
            this.startMining();
        } else {
            this.stopMining();
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {

        //createNextBlockWithAllTransactions();

        if (!miningPool.isEmpty()) {
            Block blockToMine = miningPool.getFirst();
            long previousId = 0L;
            String previousHash = "0";

            try {
                previousHash = chain.getLastBlock().getHash();
                previousId = chain.getLastBlock().getId();
            } catch (Exception e) {
                System.err.println("This is the genesis block.");
            }
            blockToMine.setId(previousId + 1);
            blockToMine.setPreviousHash(previousHash);

            System.err.println("Mining block...");
            blockToMine.mine(Runner.difficulty);

            List<Block> tmp = new ArrayList<>(chain.getAll());
            tmp.add(blockToMine);

            if (BlockchainHelper.isChainValid(tmp, Runner.difficulty)) {
                System.err.println("Block ID " + blockToMine.getId() + " is valid.");
                chain.add(blockToMine);
                storage.saveBlock(blockToMine);
                transmissionPool.add(blockToMine);
            } else {
                System.err.println("Block invalid for the chain.");
            }
        }
    }

    public void createNextBlockWithAllTransactions() {
        if (!transactionPool.isEmpty()) {
            System.out.println("Creating new block from transactions...");
            miningPool.add(new Block(transactionPool.getAll()));
        }
    }
}