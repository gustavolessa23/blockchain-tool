package com.gustavolessa.blockchain.block;

import com.gustavolessa.blockchain.services.StringUtils;

import java.security.MessageDigest;
import java.util.List;

public class BlockHelper {

    public static String calculateHash(String info){
        return StringUtils.applySha256(info);
    }

    public static boolean isChainValid(List<Block> blockchain, int difficulty){
        Block current;
        Block previous;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        // iterate through chain and check hashes
        for(int x = 1; x < blockchain.size(); x++){
            current = blockchain.get(x);
            previous = blockchain.get(x-1);

            // check calculated hash
            if(!current.hash.equals(current.getHash())){
                System.err.println("Calculated hash mismatch for block "+ current);
                return false;
            }

            // compare actual previous hash and said previous hash
            if(!previous.hash.equals(current.previousHash)){
                System.err.println("Chaining invalid. Previous hash mismatch.");
                return false;
            }

            // check if difficulty was met
            if(!current.hash.substring(0, difficulty).equals(hashTarget)){
                System.out.println("Difficulty wasn't met. Target is "+difficulty+" leading zeros.");
            }
        }
        return true;
    }

    private BlockHelper(){}
}
