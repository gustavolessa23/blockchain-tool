package com.gustavolessa.blockchain.transaction;

import java.security.PublicKey;
import java.util.Date;

public class Transaction {
//    public String hash;
//    public PublicKey sender;
//    public PublicKey recipient;
    public int type;
    public String author;
    public String message;
    public long timeStamp;

    public Transaction(int type, String author, String message) {
        this.type = type;
        this.author = author;
        this.message = message;
        this.timeStamp = new Date().getTime();
    }

    public String toString(){
        return TransactionHelper.toString(this);
    }
}
