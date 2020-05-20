package com.gustavolessa.blockchain.menu;

import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.block.Miner;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.chain.BlockchainHelper;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import com.gustavolessa.blockchain.services.application.Runner;
import com.gustavolessa.blockchain.services.data.DataValidation;
import com.gustavolessa.blockchain.services.network.Consumer;
import com.gustavolessa.blockchain.services.network.Producer;
import com.gustavolessa.blockchain.storage.StorageDAO;
import com.gustavolessa.blockchain.transaction.Transaction;
import com.gustavolessa.blockchain.transaction.TransactionHelper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

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
                "|  1   -   Display blockchain                               |",
                "|  2   -   Add new transaction                              |",
                "|  3   -   Mine next block                                  |",
                "|  4   -   Enable/disable periodically mining blocks        |",
                "|  5   -   Enable/disable periodically listening to blocks  |",
                "|  6   -   Enable/disable periodically sending blocks       |",
                "|  7   -   List transactions to be added                    |",
                "|  8   -   List blocks to be sent                           |",
                "|  9   -   List blocks to be mined                          |",
                "|  10  -   Read all blocks from storage                     |",
                "|  11  -   Save all blocks to storage                       |",
                "|  12  -   Reset blockchain                                 |",
                "|  13  -   Find block by ID                                 |",
                "|  14  -   Run testing solution / sample blockchain         |",
                "|  15  -   Initialise blockchain                            |",
                "|  16  -   Change difficulty for the miner                  |",
                "|  17  -   Exit Program                                     |"};
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
        System.out.println();
        switch (option) {
            case 1:
                System.out.println("Blockchain:");
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
                System.out.println("Transaction pool:");
                System.out.println(transactionPool.readAll());
                break;
            case 8:
                System.out.println("Transmission pool:");
                System.out.println(this.transmissionPool.readAll());
                break;
            case 9:
                System.out.println("Mining pool:");
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
                chooseDifficulty();
                break;
            case 17:
                this.forceExit();
                break;
            default:
               // System.out.println("\n*** Invalid option. Please try again ***\n");
//                    this.startMenu();
                break;
        }
        this.startMenu();
        //    }
    }

    private void chooseDifficulty() {
        int dif = intFromInput("Type the new difficulty (recommended maximum of 6). \nThis this the amount of leading zeros in the hash:");
        Runner.difficulty = dif;
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
        if(TransactionHelper.validateTransaction(t)){
            System.out.println("Generating block for new transaction...");

            Block b = new Block(List.of(t));
            transmissionPool.add(b);
        }

    }


}
