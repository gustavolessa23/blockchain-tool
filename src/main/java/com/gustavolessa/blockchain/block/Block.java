package com.gustavolessa.blockchain.block;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.transaction.Transaction;

import java.util.ArrayList;
import java.util.Date;

public class Block {

    public long id;
    public String hash;
    public String previousHash;
    private ArrayList<Transaction> data;

    public long getTimeStamp() {
        return timeStamp;
    }

    private long timeStamp;
    private int nonce;

    public Block(ArrayList<Transaction> data, String previousHash, long lastId){
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = getHash();
        this.id = lastId + 1;

    }

    public String getHash(){
        StringBuilder sb = new StringBuilder(previousHash);
        sb.append(timeStamp);
        for(int x = 0; x < data.size(); x++){
            sb.append(data.get(x));
        }
        sb.append(nonce);
        return BlockHelper.calculateHash(sb.toString());
    }

    public void mine(int difficulty){
        String target = new String(new char[difficulty]).replace('\0','0');

        while(!hash.substring(0, difficulty).equals(target)){
            nonce++;
            hash = getHash();
        }
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
