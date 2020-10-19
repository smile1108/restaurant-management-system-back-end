package com.jiac.restaurantsystem.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * FileName: SHA
 * Author: Jiac
 * Date: 2020/10/19 20:05
 */
public class SHA {

    private static final String SHA = "SHA";

    public static String getResult(String inputStr){
        System.out.println("加密前  " + inputStr);
        BigInteger bigInteger = null;
        try {
            MessageDigest sha = MessageDigest.getInstance(SHA);
            sha.update(inputStr.getBytes());
            bigInteger = new BigInteger(1, sha.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("加密后 " + bigInteger.toString());
        return bigInteger.toString();
    }
}
