package com.team7.smartwatch.shared;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Utility {

    public static boolean arrayContainsNull(Object... objects) {
    	return Arrays.asList(objects).contains(null);
    }
    
    /* SHA256 taken straight from
     * https://github.com/greatman/OKB/blob/master/src/main/java/com/greatmancode/extras/Tools.java
     */
    public static String SHA256(String message) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(message.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte element : byteData)
        {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        StringBuffer hexString = new StringBuffer();
        for (byte element : byteData)
        {
            String hex = Integer.toHexString(0xff & element);
            if (hex.length() == 1)
            {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
