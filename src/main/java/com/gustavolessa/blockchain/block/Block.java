package com.gustavolessa.blockchain.block;

import com.gustavolessa.blockchain.transaction.Transaction;

import java.util.ArrayList;
import java.util.Date;

public class Block {

    public String hash;
    public String previousHash;
    private ArrayList<Transaction> data;
    private long timeStamp;

    public Block(ArrayList<Transaction> data, String previousHash){
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
    }

}
