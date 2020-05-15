package com.gustavolessa.blockchain;

import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.block.BlockHelper;
import com.gustavolessa.blockchain.network.Consumer;
import com.gustavolessa.blockchain.network.Producer;
import com.gustavolessa.blockchain.pool.Pool;
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
import java.util.Scanner;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(BlockchainTool.class, args);
    }

    public static class BlockchainTool implements QuarkusApplication {
        @Inject
        Consumer consumer;

        @Inject
        Producer producer;

        @Inject
        StorageDAO storage;

        @Inject
        Pool pool;

        public static int difficulty = 3;

        @Override
        public int run(String... args) throws Exception {

            List<Block> blockchain = storage.readAll();

            if (blockchain.isEmpty()) {
                System.err.println("No saved data. Generating sample blockchain");
                blockchain = generateSampleBlockWithPool();
            }

            String json = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);

            System.out.println("Is the chain valid? " + BlockHelper.isChainValid(blockchain, difficulty));

            System.out.println("After" +json);

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
            System.out.println("Hash for genesis block = "+ b0.getHash());

            Block b1 = new Block(new ArrayList<Transaction>(Arrays.asList(t1)), b0.getHash(), b0.getId());
            b1.mine(difficulty);
            storage.saveBlock(b1);
            System.out.println("Hash for block 1 = "+ b1.getHash());

            Block b2 = new Block(new ArrayList<Transaction>(Arrays.asList(t2)), b1.getHash(), b1.getId());
            b2.mine(difficulty);
            storage.saveBlock(b2);
            System.out.println("Hash for block 2 = "+ b2.getHash());

            Block b3 = new Block(new ArrayList<Transaction>(Arrays.asList(t3)), b2.getHash(), b2.getId());
            b3.mine(difficulty);
            storage.saveBlock(b3);
            System.out.println("Hash for block 3 = "+ b3.getHash());

            Block b4 = new Block(new ArrayList<Transaction>(Arrays.asList(t4)), b3.getHash(), b3.getId());
            b4.mine(difficulty);
            storage.saveBlock(b4);
            System.out.println("Hash for block 3 = "+ b4.getHash());

            ArrayList<Block> blockchain = new ArrayList<>(Arrays.asList(b0,b1,b2,b3,b4));
            return blockchain;
        }

        public List<Block> generateSampleBlockWithPool(){

            Transaction t0 = new Transaction(1,"Gustavo0","Genesis0");
            Transaction t1 = new Transaction(1,"Gustavo1","Genesis1");
            Transaction t2 = new Transaction(1,"Gustavo2","Genesis2");
            Transaction t3 = new Transaction(1,"Gustavo3","Genesis3");
            Transaction t4 = new Transaction(1,"Gustavo4","Genesis4");


            //Block b0 = new Block(pool.getAll(),"0", 0);
            Block genesis = new Block(Arrays.asList(t0),"0", 1L);
            genesis.mine(difficulty);
            storage.saveBlock(genesis);
            System.out.println("Hash for genesis block = "+ genesis.getHash());

            pool.add(t0);
            pool.add(t1);
            pool.add(t2);
            pool.add(t3);
            pool.add(t4);

            ArrayList<Block> blockchain = new ArrayList<>(Arrays.asList(genesis));
            return blockchain;
        }
    }
}