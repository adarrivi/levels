package com.levels.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility builder to test different regex patterns
 * 
 * @author adarrivi
 * 
 */
public class PatternVerifierTestBuilder {

    private String regex;
    private List<String> stringsToMatch = new ArrayList<>();

    public static PatternVerifierTestBuilder newBuilder(String regex) {
        return new PatternVerifierTestBuilder(regex);
    }

    private PatternVerifierTestBuilder(String regex) {
        this.regex = regex;
    }

    public PatternVerifierTestBuilder addString(String string) {
        stringsToMatch.add(string);
        return this;
    }

    public void verifyMatchesAll() {
        if (stringsToMatch.isEmpty()) {
            throw new AssertionError("No strings to match");
        }
        for (String value : stringsToMatch) {
            if (!matchesValue(value)) {
                throw new AssertionError("The value '" + value + "' does not match.");
            }
        }
    }

    public void verifyNoMatches() {
        if (stringsToMatch.isEmpty()) {
            throw new AssertionError("No strings to match");
        }
        for (String value : stringsToMatch) {
            if (matchesValue(value)) {
                throw new AssertionError("The value '" + value + "' matches");
            }
        }
    }

    private boolean matchesValue(String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

}
