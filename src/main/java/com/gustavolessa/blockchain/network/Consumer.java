package com.gustavolessa.blockchain.network;

import com.google.gson.Gson;
import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.chain.BlockchainHelper;
import com.gustavolessa.blockchain.storage.local.LocalStorage;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Consumer class
 */
@ApplicationScoped
public class Consumer implements Runnable {

    @Inject
    ConnectionFactory connectionFactory;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private volatile String data;

    private volatile Block b;

    @Inject
    private LocalStorage storage;

    @Inject
    private Blockchain chain;

    public String getData() {
        return data;
    }

    void onStart(@Observes StartupEvent ev) {
 //       scheduler.submit(this);
        scheduler.scheduleWithFixedDelay(this, 10L, 5L, TimeUnit.SECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            JMSConsumer consumer = context.createConsumer(context.createQueue("blocks"));
            while (true) {
                TextMessage message = (TextMessage)consumer.receive();
                if (message == null) {
                    return;
                } else {
                    if (message != null) {
                      //  System.out.println("TextMessage "+message.getText());

                        String retrieved = message.getText();
                        Gson gson = new Gson();
                        Object obj = gson.fromJson(retrieved, Block.class);
                       // System.out.println("Object is "+obj.toString());
                        b = (Block) obj;
                       // System.out.println("Transaction is "+t);
                    }
                }
                System.err.println("Received block: "+ b.toString());

                if(BlockchainHelper.blockCanBeInserted(chain, b)){
                    System.err.println("Received block can be inserted!");
                    chain.add(b);
                    storage.saveBlock(b);
                } else {
                    System.err.println("Received block invalid");
                }

            //    System.err.println("Is the transaction valid? "+ TransactionHelper.validateTransaction(t));
            //    System.err.println("Is the block valid? "+ BlockHelper.isChainValid(t, Main.BlockchainTool.difficulty);

            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}