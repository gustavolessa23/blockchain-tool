package com.gustavolessa.blockchain.chain;

import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import com.gustavolessa.blockchain.block.Miner;
import com.gustavolessa.blockchain.transaction.Transaction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class SampleBlockGenerator {

    @Inject
    TransmissionPool transmissionPool;

    @Inject
    MiningPool miningPool;

    @Inject
    TransactionPool transactionPool;

    @Inject
    Blockchain blockchain;

    @Inject
    Miner miner;

//    public static MiningPool generateGenesisBlock(MiningPool pool) {
//        // Generate genesis block
//        System.err.println("Generating Genesis block...");
//        Transaction t0 = new Transaction(1, "GENESIS", "GENESIS BLOCK15");
//        Block genesis = new Block(Arrays.asList(t0), "0", 0);
//
////        System.out.println("Antes" +blockchain.size());
////        int previousSize = miningPool.getSize();
//        if (pool.add(genesis)) {
////            while(miningPool.getSize() == previousSize){
//            //miner.run();
//            try {
//                System.out.println("Waiting 15 seconds...");
//                Thread.sleep(15000);
//                //Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
////            }
//            // System.out.println("NOT SAME SIZE");
//            //     System.out.println("Meio" +blockchain.size());
//
//            //  notify();
//            //  Block mined = blockchain.get(0);
//            //   transmissionPool.add(mined);
//        }
//        return pool;
//    }

    public synchronized void generateGenesisBlock() {
        // Generate genesis block
        System.err.println("Generating Genesis block...");
        Transaction t0 = new Transaction(1, "GENESIS", "GENESIS BLOCK15");
        Block genesis = new Block(Arrays.asList(t0), "0", 0);

        miningPool.add(genesis);

    }

    public void createSampleTransactions() {
        // Sample transactions
        Transaction t1 = new Transaction(1, "Mark", "Object Oriented Constructs");
        Transaction t2 = new Transaction(1, "Greg", "Cloud Virtualization Frameworks");
        Transaction t3 = new Transaction(1, "Michael", "System Provisioning");
        Transaction t4 = new Transaction(1, "Graham", "Principles of Professional Practice");

        // Add them to pool
        transactionPool.add(t1);
        transactionPool.add(t2);
        transactionPool.add(t3);
        transactionPool.add(t4);

    }

    public void mineBlocks() {

        // Get two and add to a new block
        List<Transaction> firstTwo = transactionPool.getMany(2);
        Block b1 = new Block(
                firstTwo,
                blockchain.get(blockchain.size() - 1).getHash(),
                blockchain.get(blockchain.size() - 1).getId());
        miningPool.add(b1);

        try {
            System.out.println("Waiting three seconds.");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Transaction> secondTwo = transactionPool.getMany(2);
        Block b2 = new Block(
                secondTwo,
                blockchain.get(blockchain.size() - 1).getHash(),
                blockchain.get(blockchain.size() - 1).getId());
        miningPool.add(b2);


        try {
            System.out.println("Waiting three seconds.");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void generateSampleBlockchainWithPool() {


        //int temp =
//        synchronized (this){
        generateGenesisBlock();

        createSampleTransactions();

        mineBlocks();

//            this.notify();

//        }

    }

}
