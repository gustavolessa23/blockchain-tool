package com.gustavolessa.blockchain.block;

import com.gustavolessa.blockchain.services.StringUtils;

import java.security.MessageDigest;
import java.util.ArrayList;

public class BlockHelper {

    public static String calculateHash(String info){
        return StringUtils.applySha256(info);
    }

    public static boolean isChainValid(ArrayList<Block> blockchain, int difficulty){
        Block current;
        Block previous;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        // iterate through chain and check hashes
        for(int x = 1; x < blockchain.size(); x++){
            current = blockchain.get(x);
            previous = blockchain.get(x-1);

            // check calculated hash
            if(!current.hash.equals(current.getHash())){
                System.out.println("Calculated hash mismatch.");
                return false;
            }

            // compare actual previous hash and said previous hash
            if(!previous.hash.equals(current.previousHash)){
                System.out.println("Chaining invalid. Previous hash mismatch.");
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
