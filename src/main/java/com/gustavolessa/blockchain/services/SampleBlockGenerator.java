package com.gustavolessa.blockchain.services;

import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.transaction.Transaction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static com.gustavolessa.blockchain.Main.BlockchainTool.difficulty;
@ApplicationScoped
public class SampleBlockGenerator {

    @Inject
    MiningPool miningPool;

    @Inject
    TransactionPool transactionPool;

    @Inject
    Blockchain blockchain;

    public synchronized void generateSampleBlockchainWithPool(){

        // Generate genesis block
        System.err.println("Generating Genesis block...");
        Transaction t0 = new Transaction(1,"Gustavo","GENESIS");
        Block genesis = new Block(Arrays.asList(t0),"0", 0);
//        genesis.mine(difficulty);
        miningPool.add(genesis);

        while(blockchain.size()==0){
            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
        System.out.println("BLOCK SIZEEEEEEE "+blockchain.size());

        // Get two and add to a new block
        List<Transaction> firstTwo = transactionPool.getTransactions(2);
        Block b1 = new Block(
                firstTwo,
                blockchain.get(blockchain.size()-1).getHash(),
                blockchain.get(blockchain.size()-1).getId());
        miningPool.add(b1);

        List<Transaction> secondTwo = transactionPool.getTransactions(2);
        Block b2 = new Block(
                secondTwo,
                blockchain.get(blockchain.size()-1).getHash(),
                blockchain.get(blockchain.size()-1).getId());
        miningPool.add(b2);
        notify();
      //  b1.mine(difficulty);
    //    blockPool.add(b1);
    //    blockchain.add(b1);

//            ArrayList<Block> blockchain = new ArrayList<>(Arrays.asList(genesis));
  //      return blockchain;
    }
}
