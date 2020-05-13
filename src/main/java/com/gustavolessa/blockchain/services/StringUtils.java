package com.gustavolessa.blockchain.services;

import java.security.MessageDigest;

public class StringUtils {
    public static String applySha256(String input){
        try{
            // create a digest using SHA256 algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // apply hash to the input
            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            // create a hexadecimal String representation of the hash
            StringBuffer hexStr = new StringBuffer();
            for(int i = 0; i < hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);

                if(hex.length() == 1) hexStr.append('0');

                hexStr.append(hex);
            }
            return hexStr.toString();


        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
