package com.gustavolessa.blockchain.transaction;

import com.gustavolessa.blockchain.block.BlockHelper;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Date;

public class Transaction implements Serializable {
//    public String hash;
//    public PublicKey sender;
//    public PublicKey recipient;
    private String hash;
    private int type;
    private String author;
    private String message;
    private long timeStamp;


    public Transaction(int type, String author, String message) {
        this.type = type;
        this.author = author;
        this.message = message;
        this.timeStamp = new Date().getTime();
        this.hash = TransactionHelper.calculateHash(this);
    }

    public String calculateHash(){
        StringBuilder sb = new StringBuilder(type);
        sb.append(author);
        sb.append(message);
        sb.append(timeStamp);

        return TransactionHelper.hash(sb.toString());
    }
    public String toString(){
        return TransactionHelper.toString(this);
    }

    public int getType() {
        return this.type;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getMessage() {
        return this.message;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public String getHash() {
        return this.hash;
    }
}
