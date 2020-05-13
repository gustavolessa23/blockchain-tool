package com.gustavolessa.blockchain;

import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.block.BlockHelper;
import com.gustavolessa.blockchain.transaction.Transaction;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import com.google.gson.GsonBuilder;


import java.util.ArrayList;
import java.util.Arrays;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(BlockchainTool.class, args);
    }

    public static class BlockchainTool implements QuarkusApplication {

        public static int difficulty = 6;

        @Override
        public int run(String... args) throws Exception {

            Transaction t0 = new Transaction(1,"Gustavo0","Genesis0");
            Transaction t1 = new Transaction(1,"Gustavo1","Genesis1");
            Transaction t2 = new Transaction(1,"Gustavo2","Genesis2");
            Transaction t3 = new Transaction(1,"Gustavo3","Genesis3");
            Transaction t4 = new Transaction(1,"Gustavo4","Genesis4");

            Block b0 = new Block(new ArrayList<Transaction>(Arrays.asList(t0)),"0");
            b0.mine(difficulty);
            System.out.println("Hash for genesis block = "+b0.hash);

            Block b1 = new Block(new ArrayList<Transaction>(Arrays.asList(t1)),b0.hash);
            b1.mine(difficulty);
            System.out.println("Hash for block 1 = "+b1.hash);

            Block b2 = new Block(new ArrayList<Transaction>(Arrays.asList(t2)),b1.hash);
            b2.mine(difficulty);
            System.out.println("Hash for block 2 = "+b2.hash);

            Block b3 = new Block(new ArrayList<Transaction>(Arrays.asList(t3)),b2.hash);
            b3.mine(difficulty);
            System.out.println("Hash for block 3 = "+b3.hash);

            Block b4 = new Block(new ArrayList<Transaction>(Arrays.asList(t4)),b3.hash);
            b4.mine(difficulty);
            System.out.println("Hash for block 3 = "+b4.hash);

            ArrayList<Block> blockchain = new ArrayList<>(Arrays.asList(b0,b1,b2,b3,b4));

            String json = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);

            System.out.println("Is the chain valid? " + BlockHelper.isChainValid(blockchain, difficulty));

            System.out.println(json);

            Quarkus.waitForExit();
            return 0;
        }
    }
}