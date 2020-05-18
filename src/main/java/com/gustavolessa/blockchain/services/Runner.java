package com.gustavolessa.blockchain.services;

import com.google.common.collect.Lists;
import com.gustavolessa.blockchain.Miner;
import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.block.BlockHelper;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.chain.BlockchainHelper;
import com.gustavolessa.blockchain.network.Consumer;
import com.gustavolessa.blockchain.network.Producer;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import com.gustavolessa.blockchain.storage.StorageDAO;
import com.gustavolessa.blockchain.transaction.Transaction;
import io.quarkus.runtime.Quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
    TransactionPool transactionPool;

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

    public void readOrGenerateGenesis(){
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
                System.out.println("Generating sample blockchain.");
                generator.generateSampleBlockchainWithPool();
            }
        }
    }


    public boolean isInit(){
        return init;
    }

    public void runTest(){
        if(!init){
            init();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sendBlockFromOutside();
    }
    public void sendBlockFromOutside(){
        System.out.println("Creating block to send without adding to the chain...");
        Transaction t2 = new Transaction(1,"Jenny","Programming");
        Block toSend = new Block(Arrays.asList(t2));
        System.out.println("JENNY " + toSend);

        long previousId = 0L;
        String previousHash = "0";
        try {
            previousHash = chain.getLastBlock().getHash();
            previousId = chain.getLastBlock().getId();
        } catch (Exception e) {
            System.err.println("Shouldn't read this!!!");
        }
        toSend.setId(previousId+1);
        toSend.setPreviousHash(previousHash);
        System.err.println("Mining block...");
        if(toSend.mine(Runner.difficulty)){
            List<Block> tmp = Lists.newArrayList(chain.getAll());
            tmp.add(toSend);
            if(BlockchainHelper.isChainValid(tmp, Runner.difficulty)){
                System.err.println("Block ID "+toSend.getId()+ " is valid.");
                transmissionPool.add(toSend);
            } else {
                System.err.println("Block invalid for the chain.");
            }
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
