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
            Block b = new Block(Arrays.asList(transactionPool.getFirst()));
            miningPool.add(b);


            System.out.println("Is the chain valid? " + BlockHelper.isChainValid(chain.getMined(), difficulty));

            System.out.println("After" + chain);


            // Create a block outside architecture and send to be sent --> that means we will receive
            // a valid block that is not present in our blockchain.
            try{
                System.out.println("Sleeping for 30 seconds");
                Thread.sleep(100000);
                sendBlockOutside();
            }catch (Exception e){}



            Quarkus.waitForExit();

            return 0;
        }

        public void sendBlockOutside(){
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
        }

    }
}