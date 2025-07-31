package com.security.spring.utils;

import java.util.UUID;

public class UUIDGenerator {
    public static String generateUUID() {
        // Generates a random UUID
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
