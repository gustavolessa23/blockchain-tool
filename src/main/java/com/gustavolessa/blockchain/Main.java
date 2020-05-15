package com.gustavolessa.blockchain;

import com.google.common.collect.Lists;
import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.block.BlockHelper;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import com.gustavolessa.blockchain.services.SampleBlockGenerator;
import com.gustavolessa.blockchain.storage.StorageDAO;
import com.gustavolessa.blockchain.transaction.Transaction;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(BlockchainTool.class, args);
    }

    public static class BlockchainTool implements QuarkusApplication {

        @Inject
        Blockchain chain;

        @Inject
        StorageDAO storage;

        @Inject
        SampleBlockGenerator generator;

        @Inject
        TransmissionPool transmissionPool;

        @Inject
        TransactionPool transactionPool;

        @Inject
        MiningPool miningPool;

        public static final int difficulty = 4;

        @Override
        public int run(String... args) throws Exception {

            List<Block> fromStorage = storage.readAll();

            if (fromStorage.isEmpty()) {
                System.err.println("No saved data. Generating sample blockchain.");
                generator.generateSampleBlockchainWithPool();
            } else {
                System.err.println("Reading blocks from storage.");
                for(Block b : fromStorage) {
                    transmissionPool.add(b);
                    chain.add(b);
                    Thread.sleep(3000);
                }
            }

            // Send a block using the architecture
            Transaction t1 = new Transaction(1,"Amílcar","Design Patterns");
            transactionPool.add(t1);
            Block b = new Block(Arrays.asList(transactionPool.getFirstTransaction()));
            miningPool.add(b);


            System.out.println("Is the chain valid? " + BlockHelper.isChainValid(chain.getMined(), difficulty));

            System.out.println("After" + chain);


            // Create a block outside architecture and send to be sent --> that means we will receive
            // a valid block that is not present in our blockchain.
            try{
                System.out.println("Sleeping for 30 seconds");
                Thread.sleep(120000);
            }catch (Exception e){}

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
            System.err.println("Mining block to send ...");
            if(toSend.mine(Main.BlockchainTool.difficulty)){
                System.out.println("JENNY " + toSend);
                List<Block> tmp = Lists.newArrayList(chain.getMined());
                tmp.add(toSend);
                //  System.out.println("TEMP CHAIN "+tmp);
                if(BlockHelper.isChainValid(tmp, Main.BlockchainTool.difficulty)){
                    System.err.println("BLOCK TO SEND VALID: ID "+toSend.getId());
                    transmissionPool.add(toSend);
                } else {
                    System.err.println("Block invalid for the chain.");
                }
            }

            Quarkus.waitForExit();

            return 0;
        }

//        public List<Block> generateSampleBlock(){
//
//            Transaction t0 = new Transaction(1,"Gustavo0","Genesis0");
//            Transaction t1 = new Transaction(1,"Gustavo1","Genesis1");
//            Transaction t2 = new Transaction(1,"Gustavo2","Genesis2");
//            Transaction t3 = new Transaction(1,"Gustavo3","Genesis3");
//            Transaction t4 = new Transaction(1,"Gustavo4","Genesis4");
//
//            Block b0 = new Block(new ArrayList<Transaction>(Arrays.asList(t0)),"0", 0);
//            b0.mine(difficulty);
//
//            storage.saveBlock(b0);
//            System.out.println("Hash for genesis block = "+ b0.getHash());
//
//            Block b1 = new Block(new ArrayList<Transaction>(Arrays.asList(t1)), b0.getHash(), b0.getId());
//            b1.mine(difficulty);
//            storage.saveBlock(b1);
//            System.out.println("Hash for block 1 = "+ b1.getHash());
//
//            Block b2 = new Block(new ArrayList<Transaction>(Arrays.asList(t2)), b1.getHash(), b1.getId());
//            b2.mine(difficulty);
//            storage.saveBlock(b2);
//            System.out.println("Hash for block 2 = "+ b2.getHash());
//
//            Block b3 = new Block(new ArrayList<Transaction>(Arrays.asList(t3)), b2.getHash(), b2.getId());
//            b3.mine(difficulty);
//            storage.saveBlock(b3);
//            System.out.println("Hash for block 3 = "+ b3.getHash());
//
//            Block b4 = new Block(new ArrayList<Transaction>(Arrays.asList(t4)), b3.getHash(), b3.getId());
//            b4.mine(difficulty);
//            storage.saveBlock(b4);
//            System.out.println("Hash for block 3 = "+ b4.getHash());
//
//            ArrayList<Block> blockchain = new ArrayList<>(Arrays.asList(b0,b1,b2,b3,b4));
//            return blockchain;
//        }

    }
}