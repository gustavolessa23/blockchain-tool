package com.gustavolessa.blockchain;

import com.gustavolessa.blockchain.menu.MainMenu;
import com.gustavolessa.blockchain.services.application.Runner;
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
        Runner runner;

        @Inject
        MainMenu menu;


        @Override
        public int run(String... args) throws Exception {

            runner.init(); // initialise services

            menu.startMenu(); // start menu

            return 0;
        }
    }
}