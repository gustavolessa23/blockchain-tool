package com.gustavolessa.blockchain.transaction;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {

    private String hash;
    private int type;
    private String author;
    private String message;
    private long timeStamp;

    public Transaction() {

    }

    public Transaction(int type, String author, String message) {
        this.type = type;
        this.author = author;
        this.message = message;
        this.init();
    }

    private void init() {
        this.timeStamp = new Date().getTime();
        this.hash = TransactionHelper.calculateHash(this);
    }

    public String calculateHash() {
        StringBuilder sb = new StringBuilder(type);
        sb.append(author);
        sb.append(message);
        sb.append(timeStamp);

        return TransactionHelper.hash(sb.toString());
    }

    public String toString() {
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

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setType(int type) {
        this.type = type;
        this.init();
    }

    public void setAuthor(String author) {
        this.author = author;
        this.init();
    }

    public void setMessage(String message) {
        this.message = message;
        this.init();
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        this.hash = TransactionHelper.calculateHash(this);
    }
}
