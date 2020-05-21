package com.gustavolessa.blockchain.services.network;


import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import io.quarkus.runtime.ShutdownEvent;

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

/**
 * Producer class, that sends blocks that were added to the transmission pool.
 */
@ApplicationScoped
public class Producer implements Runnable {

    @Inject
    TransmissionPool pool;

    @Inject
    ConnectionFactory connectionFactory;

    private Block b;

    private ScheduledExecutorService scheduler;

    public Producer() {
        this.resetExecutor();
    }

    // resets the executor
    public void resetExecutor() {
        if (scheduler != null) scheduler.shutdown();

        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    // start sending blocks
    public void startSending() {
        if (scheduler != null) scheduler.shutdown();
        resetExecutor();
        System.out.println("Starting to send new blocks...");
        scheduler.scheduleWithFixedDelay(this, 0L, 300L, TimeUnit.MILLISECONDS);
    }

    // stop sending blocks
    public void stopSending() {
        System.out.println("Stopping to send new blocks...");
        scheduler.shutdown();
    }

    // switch executor on/off, accordingly to the previous state.
    public void flipSwitch() {
        if (scheduler.isShutdown()) {
            startSending();
        } else {
            stopSending();
        }
    }

    // on system shut down, shutdown the executor as well.
    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    /**
     * Send blocks to the messaging service
     */
    @Override
    public void run() {
        if (!pool.isEmpty()) {      // if pool is not empty
            b = pool.getFirst();    // get first block
            try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) { // get connection
                System.out.println("Sending block ID " + b.getId());
                TextMessage message = context.createTextMessage(b.toString()); // create message
                context.createProducer().send(context.createTopic("blocks"), message); // add to specific topic and send it
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}