package com.gustavolessa.blockchain.services.network;

import com.google.gson.Gson;
import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.chain.BlockchainHelper;
import com.gustavolessa.blockchain.storage.local.LocalStorage;
import io.quarkus.runtime.ShutdownEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Consumer class that periodically listens to blocks from the network and tries to add them to the blockchain.
 */
@ApplicationScoped
public class Consumer implements Runnable {

    @Inject
    ConnectionFactory connectionFactory;

    private ScheduledExecutorService scheduler;

    private volatile Block b;

    @Inject
    private LocalStorage storage;

    @Inject
    private Blockchain chain;

    public Consumer() {
        this.resetExecutor();
    }

    // create new executor
    public void resetExecutor() {
        if (scheduler != null) scheduler.shutdown();

        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    // start listening to blocks
    public void startListening() {
        if (scheduler != null) scheduler.shutdown();
        this.resetExecutor();
        System.out.println("Starting to listen to new blocks...");
        scheduler.scheduleWithFixedDelay(this, 0, 1L, TimeUnit.SECONDS);
    }

    // stop listening to blocks
    public void stopListening() {
        System.out.println("Stopping to listen to new blocks...");
        scheduler.shutdown();
        // resetExecutor();
    }

    // flip the switch, turning off when on.
    public void flipSwitch() {
        if (scheduler.isShutdown()) {
            this.startListening();
        } else {
            this.stopListening();
        }
    }

    // close service on application shutdown
    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    /**
     * Method to retrieve blocks from the network.
     */
    @Override
    public void run() {
        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) { // create connection
            JMSConsumer consumer = context.createConsumer(context.createTopic("blocks")); // set message topic (all members receive all messages)
          //  while (true) {
                TextMessage message = (TextMessage) consumer.receive();
                if (message == null) {
                    return;
                } else {
                    if (message != null) {
                        String retrieved = message.getText();
                        Gson gson = new Gson();
                        Object obj = gson.fromJson(retrieved, Block.class);
                        b = (Block) obj;
                    }
                }
                System.err.println("Received block ID " + b.getId());


                if (chain.getByHash(b.getHash()) == null) {
                    if (BlockchainHelper.blockCanBeInserted(chain, b)) {
                        System.err.println("Received block can be inserted!");
                        chain.add(b);
                        storage.saveBlock(b);
                    } else {
                        System.err.println("Received block invalid");
                    }
                } else {
                    System.err.println("Received block is already in the blockchain.");
                }
           // }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}