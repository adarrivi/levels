package com.levels.service.impl;

import java.util.Random;

import com.levels.service.KeyGenerator;

/**
 * Singleton class that returns a 'Reasonably unique' and alphabetical random
 * identifier with 7 chars.
 * 
 * @author adarrivi
 * 
 */
class KeyGeneratorReasonablyUnique implements KeyGenerator {

    private static final int ID_LENGTH = 7;
    private static final String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random RANDOM = new Random();

    KeyGeneratorReasonablyUnique() {
        // Only SingletonFactory (and Unit tests) should have access to the
        // constructor
    }

    @Override
    public String generateUniqueKey() {
        return createRandomAlphabeticalString(ID_LENGTH);
    }

    private String createRandomAlphabeticalString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomCharPosition = RANDOM.nextInt(ALLOWED_CHARS.length());
            sb.append(ALLOWED_CHARS.charAt(randomCharPosition));
        }
        return sb.toString();
    }

}
