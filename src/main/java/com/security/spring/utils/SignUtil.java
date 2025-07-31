package com.security.spring.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUtil {

    // Generate the signature
    public static String createSignatureForRequest(String operatorCode, long requestTime, String methodName, String secretKey) {
        String combinedString = requestTime +  secretKey + methodName + operatorCode;
        return generateMD5Hash(combinedString);
    }

    public static String createSignatureForCallBack(String operatorCode, long requestTime, String methodName, String secretKey) {
        String combinedString = operatorCode +  requestTime + methodName + secretKey;
        return generateMD5Hash(combinedString);
    }


    // Verify the signature
    public static boolean verifySignature(String operatorCode, int requestTime, String methodName, String secretKey, String sign) {
        // Recreate the expected signature
        String expectedSignature = createSignatureForCallBack(operatorCode, requestTime, methodName, secretKey);

        // Compare the recreated signature with the given signature
        return expectedSignature.equals(sign);
    }

    // MD5 hash generation
    private static String generateMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(number.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, '0');
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found!", e);
        }
    }
}
