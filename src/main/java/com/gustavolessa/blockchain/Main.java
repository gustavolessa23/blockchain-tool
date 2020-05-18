package com.gustavolessa.blockchain;

import com.google.common.collect.Lists;
import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.block.BlockHelper;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.menu.MainMenu;
import com.gustavolessa.blockchain.menu.Menu;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import com.gustavolessa.blockchain.services.Runner;
import com.gustavolessa.blockchain.services.SampleBlockGenerator;
import com.gustavolessa.blockchain.storage.StorageDAO;
import com.gustavolessa.blockchain.transaction.Transaction;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.inject.Inject;

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

        @Inject
        Runner runner;

        @Inject
        MainMenu menu;


        @Override
        public int run(String... args) throws Exception {

            runner.init();

            menu.startMenu();

           // Quarkus.waitForExit();

            return 0;
        }
    }
}