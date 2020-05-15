package com.gustavolessa.blockchain.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.*;

import com.google.gson.Gson;
import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.transaction.Transaction;
import com.gustavolessa.blockchain.transaction.TransactionHelper;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;


@ApplicationScoped
public class Consumer implements Runnable {

    @Inject
    ConnectionFactory connectionFactory;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private volatile String data;

    private volatile Transaction t;

    private volatile Block block;

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
            JMSConsumer consumer = context.createConsumer(context.createQueue("transactions"));
            while (true) {
                TextMessage message = (TextMessage)consumer.receive();
                if (message == null) {
                    return;
                } else {
                    if (message != null) {
                      //  System.out.println("TextMessage "+message.getText());

                        String retrieved = message.getText();
                        Gson gson = new Gson();
                        Object obj = gson.fromJson(retrieved, Transaction.class);
                       // System.out.println("Object is "+obj.toString());
                        t = (Transaction) obj;
                       // System.out.println("Transaction is "+t);
                    }
                }
                System.err.println("Received Transaction: "+t.toString());
            //    System.err.println("Is the transaction valid? "+ TransactionHelper.validateTransaction(t));
                System.err.println("Is the transaction valid? "+ t.calculateHash().equals(t.getHash()));

            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}