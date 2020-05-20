package com.gustavolessa.blockchain.transaction;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.services.data.StringUtils;

public class TransactionHelper {
    public static String toString(Transaction transaction) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(transaction);
    }

    private TransactionHelper() {
    }

    public static String hash(String transaction) {
        return StringUtils.applySha256(transaction);
    }

    public static String calculateHash(Transaction t) {
        StringBuilder sb = new StringBuilder(t.getType());
        sb.append(t.getAuthor());
        sb.append(t.getMessage());
        sb.append(t.getTimeStamp());
        return TransactionHelper.hash(sb.toString());
    }

    public static boolean validateTransaction(Transaction t){

        boolean ans = t.getHash().equals(calculateHash(t));
        if(ans){
            System.out.println("Transaction is valid.");
        } else {
            System.out.println("Transaction is not valid.");
        }
        return ans;
    }
}
