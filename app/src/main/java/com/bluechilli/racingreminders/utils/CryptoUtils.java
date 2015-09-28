package com.bluechilli.racingreminders.utils;

/**
 * Created by monishi on 13/01/15.
*/
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

    private static final String ALGORISM = "hmacSHA256";

    public static String get(String str, String salt) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(salt.getBytes(), ALGORISM);
        try {
            Mac mac = Mac.getInstance(ALGORISM);
            mac.init(secretKeySpec);
            byte[] result = mac.doFinal(str.getBytes());
            return byteToString(result);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String byteToString(byte [] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int d = b[i];
            d += (d < 0)? 256 : 0;
            if (d < 16) {
                buffer.append("0");
            }
            buffer.append(Integer.toString(d, 16));
        }
        return buffer.toString();
    }

}