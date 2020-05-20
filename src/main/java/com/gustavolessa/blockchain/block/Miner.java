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
 * Miner class
 */
@ApplicationScoped
public class Miner implements Runnable {

    private ScheduledExecutorService scheduler; // service to schedule tasks

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

    /**
     * Start listening to blocks being added to the mining pool
     */
    public void startMining() {
        scheduler.shutdown();
        this.resetExecutor();
        System.out.println("Starting to mine new blocks...");
        scheduler.scheduleWithFixedDelay(this, 0, 3L, TimeUnit.SECONDS);
    }

    /**
     * Stop listening to blocks.
     */
    public void stopMining() {
        System.out.println("Stopping to mine new blocks...");
        scheduler.shutdown();
        //  this.resetExecutor();
    }

    /**
     * Switch on if off and vice versa.
     */
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

    /**
     * Checks for blocks to mine, mine one, add to the blockchain, save to the storage and add to the transmission pool.
     */
    @Override
    public void run() {

        if (!miningPool.isEmpty()) { // if there is a block
            Block blockToMine = miningPool.getFirst(); // the the first
            long previousId = 0L;
            String previousHash = "0";

            try {
                previousHash = chain.getLastBlock().getHash(); // get the hash and ID
                previousId = chain.getLastBlock().getId();
            } catch (Exception e) {
                System.err.println("This is the genesis block.");
            }
            blockToMine.setId(previousId + 1); // set the ID and reference to the previous block
            blockToMine.setPreviousHash(previousHash);

            System.err.println("Mining block...");
            blockToMine.mine(Runner.difficulty); // mine the block

            List<Block> tmp = new ArrayList<>(chain.getAll()); // create a new blockchain using the existing data
            tmp.add(blockToMine); // add the mined block

            if (BlockchainHelper.isChainValid(tmp, Runner.difficulty)) { // check if they form a correct chain
                System.err.println("Block ID " + blockToMine.getId() + " is valid.");
                chain.add(blockToMine); // add to the chain
                storage.saveBlock(blockToMine); // save to storage
                transmissionPool.add(blockToMine); // add to transmission pool
            } else {
                System.err.println("Block invalid for the chain.");
            }
        }
    }

    /**
     * Creates and mines a block with the first transaction of the transaction pool, if existent.
     */
    public void createNextBlockWithAllTransactions() {
        if (!transactionPool.isEmpty()) {
            System.out.println("Creating new block from transactions...");
            miningPool.add(new Block(transactionPool.getAll()));
        }
    }
}