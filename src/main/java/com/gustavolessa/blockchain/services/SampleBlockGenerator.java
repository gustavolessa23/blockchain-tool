package com.gustavolessa.blockchain.services;

import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.pool.TransmissionPool;
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

    public void generateGenesisBlock(){
        // Generate genesis block
        System.err.println("Generating Genesis block...");
        Transaction t0 = new Transaction(1,"GENESIS","GENESIS BLOCK15");
        Block genesis = new Block(Arrays.asList(t0),"0", 0);
        miningPool.add(genesis);

        while(blockchain.size()==0){
            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }

        Block mined = blockchain.get(0);
        transmissionPool.add(mined);


    }

    public void createSampleTransactions(){
        // Sample transactions
        Transaction t1 = new Transaction(1,"Mark","Object Oriented Constructs");
        Transaction t2 = new Transaction(1,"Greg","Cloud Virtualization Frameworks");
        Transaction t3 = new Transaction(1,"Michael","System Provisioning");
        Transaction t4 = new Transaction(1,"Graham","Principles of Professional Practice");

        // Add them to pool
        transactionPool.add(t1);
        transactionPool.add(t2);
        transactionPool.add(t3);
        transactionPool.add(t4);

    }

    public void mineBlocks(){
        // Get two and add to a new block
        List<Transaction> firstTwo = transactionPool.getMany(2);
        Block b1 = new Block(
                firstTwo,
                blockchain.get(blockchain.size()-1).getHash(),
                blockchain.get(blockchain.size()-1).getId());
        miningPool.add(b1);

        List<Transaction> secondTwo = transactionPool.getMany(2);
        Block b2 = new Block(
                secondTwo,
                blockchain.get(blockchain.size()-1).getHash(),
                blockchain.get(blockchain.size()-1).getId());
        miningPool.add(b2);
    }

    public synchronized void generateSampleBlockchainWithPool(){

        generateGenesisBlock();

        createSampleTransactions();

        mineBlocks();

        notify();

    }

}
