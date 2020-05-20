package com.gustavolessa.blockchain.services.application;

import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.block.Miner;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.chain.BlockchainHelper;
import com.gustavolessa.blockchain.chain.SampleBlockGenerator;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import com.gustavolessa.blockchain.services.network.Consumer;
import com.gustavolessa.blockchain.services.network.Producer;
import com.gustavolessa.blockchain.storage.StorageDAO;
import com.gustavolessa.blockchain.transaction.Transaction;
import io.quarkus.runtime.Quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@ApplicationScoped
public class Runner {

    public static int difficulty = 4;

    @Inject
    Consumer consumer;

    @Inject
    Producer producer;

    @Inject
    Blockchain chain;

    @Inject
    TransmissionPool transmissionPool;

    @Inject
    MiningPool miningPool;

    @Inject
    Miner miner;

    @Inject
    StorageDAO storage;

    @Inject
    SampleBlockGenerator generator;

    boolean init = false;

    public void init() {
        miner.startMining();
        producer.startSending();
        consumer.startListening();
        init = true;
    }

    public void readOrGenerateGenesis() {
        System.out.println("Reading saved blocks.");
        List<Block> fromStorage = storage.readAll();

        if (fromStorage.isEmpty()) {
            System.out.println("No blocks found.");
            generator.generateGenesisBlock();
        } else {
            System.err.println("Reading blocks from storage.");
            for (Block b : fromStorage) {
                transmissionPool.add(b);
                chain.add(b);
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
            if (!BlockchainHelper.isChainValid(chain.getAll(), difficulty)) {
                System.err.println("Blockchain invalid --> resetting");
                chain.reset();
                storage.clear();
                generator.generateGenesisBlock();
            }
        }
    }


    public void runTest() {

        if (!init) {
            init();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Generating sample blockchain.");
        generator.generateGenesisBlock();

        generator.createSampleTransactions();
//
        try {
         //   System.out.println("Wait: 8");
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        generator.mineBlocks();

        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendBlockFromOutside();
    }

    public void sendBlockFromOutside() {
        System.out.println("Creating block to send without adding to the chain...");
        Transaction t2 = new Transaction(1, "Jenny", "Programming");

        Block toSend = new Block(Arrays.asList(t2));
        System.out.println("JENNY " + toSend);

        long previousId = 0L;
        String previousHash = "0";
        try {
            previousHash = chain.getLastBlock().getHash();
            previousId = chain.getLastBlock().getId();
        } catch (Exception e) {
//            System.err.println("Shouldn't read this!!!");
        }
        toSend.setId(previousId + 1);
        toSend.setPreviousHash(previousHash);

        System.err.println("Mining fake block...");
        if (toSend.mine(Runner.difficulty)) {

            System.out.println("Changing the block ID...");
            toSend.setId(999);

            List<Block> tmp = new ArrayList<>(chain.getAll());
            tmp.add(toSend);
            transmissionPool.add(toSend);


        }
    }

    public void exitApplication() {
        System.err.println("Shutting down Blockchain Demonstration Tool");
        consumer.stopListening();
        producer.stopSending();
        miner.stopMining();
        Quarkus.asyncExit();
    }


}
