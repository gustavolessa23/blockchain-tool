package com.gustavolessa.blockchain.transaction;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.services.data.StringUtils;

/**
 * Helper class to perform actions in the transactions
 */
public class TransactionHelper {

    // print in json format
    public static String toString(Transaction transaction) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(transaction);
    }

    private TransactionHelper() {
    }

    // hash a transaction using a String
    public static String hash(String transaction) {
        return StringUtils.applySha256(transaction);
    }

    // calculate the hash of a transaction object
    public static String calculateHash(Transaction t) {
        StringBuilder sb = new StringBuilder(t.getType());
        sb.append(t.getAuthor());
        sb.append(t.getMessage());
        sb.append(t.getTimeStamp());
        return TransactionHelper.hash(sb.toString());
    }

    /**
     * Validates transactions by checking their hash.
     * @param t
     * @return
     */
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
