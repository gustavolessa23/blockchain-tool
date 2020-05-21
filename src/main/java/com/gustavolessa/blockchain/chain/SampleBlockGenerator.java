package com.gustavolessa.blockchain.chain;

import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.transaction.Transaction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * Generates sample information for the blockchain.
 */
@ApplicationScoped
public class SampleBlockGenerator {


    @Inject
    MiningPool miningPool;

    @Inject
    TransactionPool transactionPool;

    @Inject
    Blockchain blockchain;

    /**
     * Generate genesis block, the first block of the chain.
     */
    public synchronized void generateGenesisBlock() {
        // Generate genesis block
        System.err.println("Generating Genesis block...");
        Transaction t0 = new Transaction(1, "GENESIS", "GENESIS BLOCK15");
        Block genesis = new Block(Arrays.asList(t0), "0", 0);

        miningPool.add(genesis);

    }

    /**
     * Create sample transactions.
     */
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

    /**
     * Creates sample blocks and adds them to the mining pool.
     */
    public void mineSampleBlocks() {

        // Get two and add to a new block
        List<Transaction> firstTwo = transactionPool.getMany(2);
        Block b1 = new Block(
                firstTwo,
                blockchain.get(blockchain.size() - 1).getHash(),
                blockchain.get(blockchain.size() - 1).getId());
        miningPool.add(b1);

        try {
          //  System.out.println("Waiting three seconds.");
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
        //    System.out.println("Waiting three seconds.");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
