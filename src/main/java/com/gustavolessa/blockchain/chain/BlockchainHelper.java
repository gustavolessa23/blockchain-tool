package com.gustavolessa.blockchain.chain;

import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.block.BlockHelper;
import com.gustavolessa.blockchain.services.application.Runner;
import com.gustavolessa.blockchain.storage.StorageDAO;

import java.util.ArrayList;
import java.util.List;

public class BlockchainHelper {

    private BlockchainHelper() {
    }

    public static boolean isChainValid(List<Block> blockchain, int difficulty) {
        Block current;
        Block previous;

        // iterate through chain and check hashes
        for (int x = 1; x < blockchain.size(); x++) {
            current = blockchain.get(x);
            previous = blockchain.get(x - 1);

            // check current block's previous hash reference with previous block's hash
            // check calculated hash for current block
            // check if difficulty was met
            boolean isValid = (
                    BlockHelper.checkHashReference(current, previous) &&
                            BlockHelper.checkCalculatedHash(current) &&
                            BlockHelper.checkDifficulty(difficulty, current)
            );

            if (!isValid) return false;
        }
        return true;
    }

    public static boolean blockCanBeInserted(Blockchain chain, Block b) {
        List<Block> tmp = new ArrayList<>(chain.getAll());
        tmp.add(b);
        //  System.out.println("TEMP CHAIN "+tmp);
        if (isChainValid(tmp, Runner.difficulty)) {
            System.err.println("Block ID " + b.getId() + " can be inserted.");
            return true;
        } else {
            System.err.println("Block invalid for the chain.");
            return false;
        }
    }

    public static boolean tryToAdd(Blockchain chain, Block b) {
        boolean isAllowed = blockCanBeInserted(chain, b);
        if (isAllowed) chain.add(b);
        return isAllowed;
    }

    public static void resetBlockchain(Blockchain blockchain, StorageDAO storage) {
        System.out.println("Resetting blockchain...");
        blockchain.reset();
        storage.clear();
        storage.writeAll(blockchain.getAll());
    }

}
