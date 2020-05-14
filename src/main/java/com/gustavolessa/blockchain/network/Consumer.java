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
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;


@ApplicationScoped
public class Consumer implements Runnable {

    @Inject
    ConnectionFactory connectionFactory;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private volatile String data;

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
                if (message != null) {
                    System.out.println("Message received "+message);
                    System.out.println("TextMessage "+message.getText());

                   String retrieved = ((TextMessage) message).getText();
                    Gson gson = new Gson();
                    Object obj = gson.fromJson(retrieved, Block.class);
                    System.out.println("Object is "+obj.toString());
                    block = (Block) obj;
                    System.out.println("Block is "+block);

                    System.out.println(block);
                }
                if (message == null) return;
//                if (message instanceof ObjectMessage){

//                }

                //data = message.getBody(String.class);

                System.err.println("Received Block: "+block.toString());

               // System.err.println("Received Data: "+data);
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}