package com.gustavolessa.blockchain.network;


import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@ApplicationScoped
public class Producer implements Runnable {

    @Inject
    TransmissionPool pool;

    @Inject
    ConnectionFactory connectionFactory;

    private Block b;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    void onStart(@Observes StartupEvent ev) {
        scheduler.scheduleWithFixedDelay(this, 2L, 5L, TimeUnit.SECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        if(!pool.isEmpty()){

            b = pool.getFirstBlock();

                try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
                    System.out.println("Sending block = "+ b.toString());
                    TextMessage message = context.createTextMessage(b.toString());
//                    System.out.println("TextMessage "+message.getText());
//                    Queue queue = context.createQueue("transactions");
//                    JMSProducer producer = context.createProducer();
                  //  producer.send(context.createQueue("transactions"), message);
                    context.createProducer().send(context.createQueue("blocks"),message);
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