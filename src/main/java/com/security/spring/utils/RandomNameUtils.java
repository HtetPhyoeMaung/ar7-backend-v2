package com.security.spring.utils;

import java.security.SecureRandom;
import java.util.List;

public class RandomNameUtils {
    private static final List<String> FIRST_NAMES = List.of(
            "John", "Emma", "Liam", "Olivia", "Noah", "Sophia", "William", "Isabella", "James", "Ava",
            "Hiroshi", "Sakura", "Carlos", "Lucia", "Hans", "Elena", "Ali", "Fatima", "Yusuf", "Mei"
    );

    private static final List<String> LAST_NAMES = List.of(
            "Smith", "Johnson", "Brown", "Garcia", "Martinez", "Takahashi", "Kumar", "Kim", "Lopez", "Schmidt",
            "Gonzalez", "Rodriguez", "Chen", "Nguyen", "Hassan", "Dubois", "Petrov", "Ivanov", "Alvarez", "Singh"
    );

    private static final SecureRandom RANDOM = new SecureRandom();

//    public static String generateRandomName() {
//        String firstName = FIRST_NAMES.get(RANDOM.nextInt(FIRST_NAMES.size()));
//        String lastName = LAST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size()));
//        return firstName + " " + lastName;
//    }
}
