package com.levels.http.server;

import com.levels.exception.InvalidParameterException;

class ParameterVerifier {

    private static final String EMPTY = "";

    ParameterVerifier() {
        // Limiting scope, so it can be used only within server package
    }

    int getValueAsUnsignedInt(String value) {
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
