package com.gustavolessa.blockchain.menu;

import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.chain.BlockchainHelper;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import com.gustavolessa.blockchain.block.Miner;
import com.gustavolessa.blockchain.services.application.Runner;
import com.gustavolessa.blockchain.services.data.DataValidation;
import com.gustavolessa.blockchain.services.network.Consumer;
import com.gustavolessa.blockchain.services.network.Producer;
import com.gustavolessa.blockchain.storage.StorageDAO;
import com.gustavolessa.blockchain.transaction.Transaction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Main Menu Template
 *
 * @author Gustavo Lessa
 */
@ApplicationScoped
public class MainMenu extends Menu {

    @Inject
    Consumer consumer;

    @Inject
    Producer producer;

    @Inject
    Blockchain blockchain;

    @Inject
    TransactionPool transactionPool;

    @Inject
    TransmissionPool transmissionPool;

    @Inject
    MiningPool miningPool;

    @Inject
    Miner miner;

    @Inject
    StorageDAO storage;

    @Inject
    Runner runner;

    /**
     * Basic constructor that accepts zoodata as argument.
     */
    public MainMenu() {
        super();
        this.load();
    }

    public void load() {
        String[] options = {
                "Display blockchain",
                "Add new transaction",
                "Mine next block",
                "Enable/disable periodically mining to blocks",
                "Enable/disable periodically listening to blocks",
                "Enable/disable periodically sending blocks",
                "List transactions to be added",
                "List blocks to be sent",
                "List blocks to be mined",
                "Read all blocks from storage",
                "Save all blocks to storage",
                "Reset blockchain",
                "Find block by ID",
                "Run testing solution / sample blockchain",
                "Initialise blockchain",
                "Exit Program"};
        this.setOptions(options);
        this.setTitle("Blockchain Demonstration Tool");
        // this.startMenu();
    }

    /**
     * This method is responsible for linking the menu options with their
     * respective actions.
     */
    @Override
    public void optionSelector() {
        //  while(true){
        int option = intFromInput("Please choose an option:");
        switch (option) {
            case 1:
                System.out.println(blockchain.getAll());
                break;
            case 2:
                addTransaction();
                break;
            case 3:
                miner.createNextBlockWithAllTransactions();
                new Thread(miner).start();
                break;
            case 4:
                miner.flipSwitch();
                break;
            case 5:
                consumer.flipSwitch();
                break;
            case 6:
                producer.flipSwitch();
                break;
            case 7:
                System.out.println(transactionPool.readAll());
                break;
            case 8:
                System.out.println(this.transmissionPool.readAll());
                break;
            case 9:
                System.out.println(miningPool.readAll());
                break;
            case 10:
                storage.readAll()
                        .stream()
                        .forEach(b -> {
                            if (BlockchainHelper.tryToAdd(blockchain, b)) transmissionPool.add(b);
                        });
                break;
            case 11:
                storage.writeAll(blockchain.getAll());
                break;
            case 12:
                BlockchainHelper.resetBlockchain(blockchain, storage);
                break;
            case 13:
                blockById();
                break;
            case 14:
                //runner.runTest();
                testSolution();
                break;
            case 15:
                runner.readOrGenerateGenesis();
                break;
            case 16:
                this.forceExit();
                break;
            default:
                System.out.println("\n*** Invalid option. Please try again ***\n");
//                    this.startMenu();
                break;
        }
        this.startMenu();
        //    }
    }

    public void forceExit() {
        this.exit();
        runner.exitApplication();
    }


    public void testSolution() {
        System.out.println("The program will create sample transactions and mine three a few blocks, sending them as soon as ready.");
        System.out.println("Than, another block will be mined, however will not be inserted into the blockchain.");
        System.out.println("After mined, the block will be sent to the network and then retrieved by this node.");
        try {
            Thread.sleep(3000);
            storage.clear();
            blockchain.reset();
            runner.runTest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public int intFromInput(String s) {
        System.out.println(s);
        return DataValidation.checkForInt(super.in);
    }

    public String stringFromInput(String s) {
        System.out.println(s);
        return DataValidation.checkForString(super.in);
    }

    public void blockById() {
        int id = intFromInput("Type the ID to search: ");
        try {
            System.out.println(blockchain.getById(id));
        } catch (Exception e) {
            System.err.println("Block ID " + id + " not found.");
        }
    }

    public void addTransaction() {
        int type = intFromInput("Type the transaction type (integer): ");
        String author = stringFromInput("Type author's name: ");
        String message = stringFromInput("Type the message: ");
        Transaction t = new Transaction(type, author, message);
        transactionPool.add(t);
        System.out.println("Transaction added.");
        System.out.println(t);
    }


}
