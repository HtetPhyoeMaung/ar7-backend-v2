package com.security.spring.utils;

import java.security.SecureRandom;
import java.util.Random;

public class AR7IdGenerate {
    private static final SecureRandom random = new SecureRandom();
    public static int generateDigit() {
        return 1000000 + random.nextInt(9000000);
    }
    public static String generateKey(int totalSize) {
        String prefix = "ey";
        int randomPartSize = totalSize - prefix.length(); // 5 - 2 = 3

        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomPart = new StringBuilder();

        for (int i = 0; i < randomPartSize; i++) {
            randomPart.append(characters.charAt(random.nextInt(characters.length())));
        }

        return prefix + randomPart.toString();
    }
}
