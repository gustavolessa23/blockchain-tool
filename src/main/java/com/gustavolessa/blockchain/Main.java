package com.gustavolessa.blockchain;

import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.block.BlockHelper;
import com.gustavolessa.blockchain.storage.StorageDAO;
import com.gustavolessa.blockchain.storage.local.LocalStorage;
import com.gustavolessa.blockchain.transaction.Transaction;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import com.google.gson.GsonBuilder;


import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(BlockchainTool.class, args);
    }

    public static class BlockchainTool implements QuarkusApplication {

        @Inject
        StorageDAO storage;

        public static int difficulty = 5;

        @Override
        public int run(String... args) throws Exception {

            List<Block> blockchain = storage.readAll();
            if (blockchain.isEmpty()) {
                System.err.println("No saved data. Generating sample blockchain");
                blockchain = generateSampleBlock();
            }

            String json = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);

            System.out.println("Is the chain valid? " + BlockHelper.isChainValid(blockchain, difficulty));

            System.out.println(json);

            Quarkus.waitForExit();


            return 0;
        }

        public List<Block> generateSampleBlock(){
            Transaction t0 = new Transaction(1,"Gustavo0","Genesis0");
            Transaction t1 = new Transaction(1,"Gustavo1","Genesis1");
            Transaction t2 = new Transaction(1,"Gustavo2","Genesis2");
            Transaction t3 = new Transaction(1,"Gustavo3","Genesis3");
            Transaction t4 = new Transaction(1,"Gustavo4","Genesis4");

            Block b0 = new Block(new ArrayList<Transaction>(Arrays.asList(t0)),"0", 0);
            b0.mine(difficulty);
            storage.saveBlock(b0);
            System.out.println("Hash for genesis block = "+b0.hash);

            Block b1 = new Block(new ArrayList<Transaction>(Arrays.asList(t1)),b0.hash,b0.id);
            b1.mine(difficulty);
            storage.saveBlock(b1);
            System.out.println("Hash for block 1 = "+b1.hash);

            Block b2 = new Block(new ArrayList<Transaction>(Arrays.asList(t2)),b1.hash, b1.id);
            b2.mine(difficulty);
            storage.saveBlock(b2);
            System.out.println("Hash for block 2 = "+b2.hash);

            Block b3 = new Block(new ArrayList<Transaction>(Arrays.asList(t3)),b2.hash, b2.id);
            b3.mine(difficulty);
            storage.saveBlock(b3);
            System.out.println("Hash for block 3 = "+b3.hash);

            Block b4 = new Block(new ArrayList<Transaction>(Arrays.asList(t4)),b3.hash, b3.id);
            b4.mine(difficulty);
            storage.saveBlock(b4);
            System.out.println("Hash for block 3 = "+b4.hash);

            ArrayList<Block> blockchain = new ArrayList<>(Arrays.asList(b0,b1,b2,b3,b4));
            return blockchain;
        }
    }
}