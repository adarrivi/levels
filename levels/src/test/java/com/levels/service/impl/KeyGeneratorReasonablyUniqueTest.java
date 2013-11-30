package com.levels.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.levels.service.KeyGenerator;

public class KeyGeneratorReasonablyUniqueTest {

    private static final int UNIQUENESS = 1000;

    private static final String SPECIAL_CHARS_REGEX = "[$&+,:;=?@#|]";

    private KeyGenerator victim = new KeyGeneratorReasonablyUnique();

    // output parameters
    private String key;
    private Set<String> keySet;

    @Test
    public void generateUniqueKey_Returns7lenghId() {
        whenGenerateUniqueKey();
        thenKeyShouldHaveLength(7);
    }

    private void whenGenerateUniqueKey() {
        key = victim.generateUniqueKey();
    }

    private void thenKeyShouldHaveLength(int expectedLength) {
        Assert.assertEquals(expectedLength, key.length());
    }

    @Test
    public void generateUniqueKey_DoesNotContainStrangeChars() {
        whenGenerateUniqueKey();
        thenKeyShouldHaveLength(7);
        thenKeyShouldNotContainAny(SPECIAL_CHARS_REGEX);
    }

    private void thenKeyShouldNotContainAny(String forbiddenCharsRegex) {
        Pattern regex = Pattern.compile(forbiddenCharsRegex);
        Matcher matcher = regex.matcher(key);
        Assert.assertFalse(matcher.find());
    }

    @Test
    public void generateUniqueKey_ReasonablyUnique() {
        whenMultiplegenerateUniqueKey(UNIQUENESS);
        thenAllKeysShouldBeDifferent();
    }

    private void whenMultiplegenerateUniqueKey(int times) {
        keySet = new HashSet<>();
        for (int i = 0; i < times; i++) {
            keySet.add(victim.generateUniqueKey());
        }
    }

    private void thenAllKeysShouldBeDifferent() {
        Assert.assertEquals(UNIQUENESS, keySet.size());
    }

}
