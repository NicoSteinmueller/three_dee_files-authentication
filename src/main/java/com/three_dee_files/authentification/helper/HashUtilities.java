package com.three_dee_files.authentification.helper;

import java.security.MessageDigest;

public class HashUtilities {
    public static String hashSHA512(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA512");
            byte[] thedigest = md.digest(input.getBytes());
            return bytesToHex(thedigest);

        }catch (Exception ignored){}
        return null;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
