package com.gustavolessa.blockchain.block;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.services.data.StringUtils;
import com.gustavolessa.blockchain.transaction.Transaction;

import java.util.Date;
import java.util.List;

/**
 * Class that defines a block
 */
public class Block {

    private long id;
    private String hash;
    private String previousHash;
    private final List<Transaction> data;
    private final long timeStamp;
    private int nonce;

    public Block(List<Transaction> data, String previousHash, long lastId) {
        this.id = lastId + 1;
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime(); //get current time in mills on obj creation
        this.hash = this.calculateHash(); // calculate hash for the transaction
    }

    public Block(List<Transaction> data) {
        this.id = 0;
        this.data = data;
        this.previousHash = "";
        this.timeStamp = new Date().getTime();
        this.hash = this.calculateHash();
    }

    /**
     * Calculates the hash using SHA256
     *
     * @return
     */
    public String calculateHash() {
        StringBuilder sb = new StringBuilder(previousHash);
        sb.append(timeStamp);
        for (int x = 0; x < data.size(); x++) {
            sb.append(data.get(x));
        }
        sb.append(nonce);
        return StringUtils.applySha256(sb.toString());
    }

    /**
     * Mine the block, according to a difficulty level.
     *
     * @param difficulty
     * @return
     */
    public boolean mine(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); // create a string from the difficulty
        while (!hash.substring(0, difficulty).equals(target)) { // while calculated hash doesn't meet difficulty
            nonce++; // increase the nonce
            hash = calculateHash(); // recalculate the hash.
        }
        System.err.println("Block ID " + this.id + " mined.");
        return true;
    }

    public String getHash() {
        return hash;
    }

    public long getId() {
        return id;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public List<Transaction> getData() {
        return data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getNonce() {
        return nonce;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public void setId(long id) {
        this.id = id;
    }
}
