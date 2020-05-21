package com.gustavolessa.blockchain.pool;

import com.gustavolessa.blockchain.block.Block;

import javax.enterprise.context.ApplicationScoped;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Block queue pool, to hold blocks waiting to be transmitted.
 */
@ApplicationScoped
public class TransmissionPool extends AbstractQueuePool<Block> {

    private final Queue<Block> pool = new ConcurrentLinkedQueue<>();

    @Override
    public boolean clear() {
        pool.clear();
        if (pool.isEmpty()) {
            System.out.println("Transmission pool cleared!");
            return true;
        } else {
            System.out.println("Could not clear Block pool");
            return false;
        }
    }
}
