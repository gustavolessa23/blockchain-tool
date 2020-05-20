package com.gustavolessa.blockchain.pool;

import com.gustavolessa.blockchain.block.Block;

import javax.enterprise.context.ApplicationScoped;

/**
 * Pool of blocks to store a queue of blocks waiting to be mined.
 */
@ApplicationScoped
public class MiningPool extends AbstractQueuePool<Block> {

    @Override
    public boolean clear() {
        this.pool.clear();
        if (pool.isEmpty()) {
            System.out.println("Mining pool cleared!");
            return true;
        } else {
            System.out.println("Could not clear Block pool");
            return false;
        }
    }

}
