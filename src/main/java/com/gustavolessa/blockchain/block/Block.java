package com.gustavolessa.blockchain.block;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.transaction.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    private long id;
    private String hash;
    private String previousHash;
    private List<Transaction> data;
    private long timeStamp;
    private int nonce;

    public Block(List<Transaction> data, String previousHash, long lastId){
        this.id = lastId + 1;
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = this.calculateHash();
    }

    public String calculateHash(){
        StringBuilder sb = new StringBuilder(previousHash);
        sb.append(timeStamp);
        for(int x = 0; x < data.size(); x++){
            sb.append(data.get(x));
        }
        sb.append(nonce);
        return BlockHelper.hash(sb.toString());
    }

    public boolean mine(int difficulty){
        String target = new String(new char[difficulty]).replace('\0','0');

        while(!hash.substring(0, difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
        }
        System.out.println("MINED: "+this);
        return true;
    }

    public String getHash(){
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

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
