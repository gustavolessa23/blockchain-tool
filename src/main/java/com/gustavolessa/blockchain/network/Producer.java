package com.gustavolessa.blockchain.network;


import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.*;

import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.transaction.Transaction;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

/**
 * A bean producing random prices every 5 seconds and sending them to the prices JMS queue.
 */
@ApplicationScoped
public class Producer implements Runnable {

    @Inject
    TransactionPool pool;

    @Inject
    ConnectionFactory connectionFactory;



    private Transaction t;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    void onStart(@Observes StartupEvent ev) {
        scheduler.scheduleWithFixedDelay(this, 0L, 5L, TimeUnit.SECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        if(!pool.isEmpty()){

//                Transaction t = pool.getFirstTransaction();
            t = pool.getFirstTransaction();

                try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
                    System.out.println("Sending transaction = "+t.toString());
                    TextMessage message = context.createTextMessage(t.toString());
//                    System.out.println("TextMessage "+message.getText());
//                    Queue queue = context.createQueue("transactions");
//                    JMSProducer producer = context.createProducer();
                  //  producer.send(context.createQueue("transactions"), message);
                    context.createProducer().send(context.createQueue("transactions"),message);
                } catch(Exception e){
                    e.printStackTrace();
                }
//            toSend.forEach(e -> System.out.println("Trying to send "+e.toString()));
//            try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
//                context.createProducer()..send(context.createQueue("transactions"), toSend.toString());
//            } catch(Exception e){
//                e.printStackTrace();
//            }
        }
    }
}