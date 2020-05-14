package com.gustavolessa.blockchain.transaction;

import com.google.gson.GsonBuilder;

public class TransactionHelper {
    public static String toString(Transaction transaction){
//        StringBuilder sb = new StringBuilder();
//        sb.append(transaction.type);
//        sb.append(transaction.author);
//        sb.append(transaction.message);
//        sb.append(transaction.timeStamp);
//        return sb.toString();
        return new GsonBuilder().setPrettyPrinting().create().toJson(transaction);
    }
}
