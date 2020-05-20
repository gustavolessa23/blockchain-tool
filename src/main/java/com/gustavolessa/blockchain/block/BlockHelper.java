package com.gustavolessa.blockchain.block;

import com.gustavolessa.blockchain.services.data.StringUtils;

/**
 * Helper class for blocks
 */
public class BlockHelper {

    /**
     * Calculates the hash
     *
     * @param info
     * @return
     */
    public static String hash(String info) {
        return StringUtils.applySha256(info);
    }

    /**
     * Checks if the hash reference is correct
     *
     * @param current  block
     * @param previous block
     * @return true if it is valid
     */
    public static boolean checkHashReference(Block current, Block previous) {
        if (!previous.getHash().equals(current.getPreviousHash())) {
            System.err.println("Previous hash mismatch for block " + current.getHash() + ".");
            return false;
        }
        return true;
    }

    /**
     * @param difficulty
     * @param current
     * @return
     */
    public static boolean checkDifficulty(int difficulty, Block current) {
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        if (!current.getHash().substring(0, difficulty).equals(hashTarget)) {
            System.err.println("Difficulty wasn't met. Target is " + difficulty + " leading zeros.");
            return false;
        }
        return true;
    }

    public static boolean checkCalculatedHash(Block block) {
        StringBuilder sb = new StringBuilder(block.getPreviousHash());
        sb.append(block.getTimeStamp());

        for (int x = 0; x < block.getData().size(); x++) {
            sb.append(block.getData().get(x));
        }

        sb.append(block.getNonce());
        String calculatedHash = BlockHelper.hash(sb.toString());

        if (!block.getHash().equals(calculatedHash)) {
            System.err.println("Calculated hash mismatch for block " + block.getHash());
            return false;
        } else {
            return true;
        }
    }

    private BlockHelper() {
    }
}
