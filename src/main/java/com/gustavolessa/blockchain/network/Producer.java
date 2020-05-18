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

    private ScheduledExecutorService scheduler;
    //private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public Producer() {
        this.resetExecutor();
    }

    public void resetExecutor(){
        if (scheduler != null) scheduler.shutdown();

        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void startSending(){
        if (scheduler != null) scheduler.shutdown();
        resetExecutor();
            System.out.println("Starting to send new blocks...");
            scheduler.scheduleWithFixedDelay(this, 0L, 300L, TimeUnit.MILLISECONDS);
    }

    public void stopSending(){
        System.out.println("Stopping to send new blocks...");
        scheduler.shutdown();
    //    this.resetExecutor();
    }

    public void flipSwitch(){
        if(scheduler.isShutdown()){
            startSending();
        } else {
            stopSending();
        }
    }

    public boolean isActive(){
        return scheduler.isShutdown();
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        if(!pool.isEmpty()){
            b = pool.getFirst();
            try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
                System.out.println("Sending block ID "+ b.getId());
                TextMessage message = context.createTextMessage(b.toString());
                context.createProducer().send(context.createQueue("blocks"),message);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}