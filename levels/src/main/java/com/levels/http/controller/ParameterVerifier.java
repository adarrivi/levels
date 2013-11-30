package com.levels.http.controller;

import com.levels.exception.InvalidParameterException;

public class ParameterVerifier {

    private static final String EMPTY = "";

    ParameterVerifier() {
        // Only SingletonFactory (and Unit tests) should have access to the
        // constructor
    }

    public int getValueAsUnsignedInt(String value) {
        if (value == null || EMPTY.equals(value)) {
            throw new InvalidParameterException("The integer cannot be empty");
        }
        int integerValue;
        try {
            integerValue = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new InvalidParameterException("Invalid integer value (probably too long?): " + value, ex);
        }
        if (integerValue < 0) {
            throw new InvalidParameterException("The integer value cannot be negative: " + value);
        }
        return integerValue;
    }
}
