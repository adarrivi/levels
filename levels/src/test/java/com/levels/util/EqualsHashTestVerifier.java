package com.levels.util;

import org.junit.Assert;

/**
 * Utility class to test equals and hashcode methods
 * 
 * @author adarrivi
 * 
 */
public class EqualsHashTestVerifier {

    private Object victim;
    private Object equals;
    private Object differents[];

    public EqualsHashTestVerifier(Object victim, Object equals, Object... different) {
        this.victim = victim;
        this.equals = equals;
        this.differents = different;
    }

    public void verify() {
        verifyEqualsValue(true, equals);
        verifyDifferents();
    }

    private void verifyEqualsValue(boolean value, Object item) {
        assertItemsNotNull(item);
        verifyNotEqualsToNull();
        verifyNotEqualsToDifferentClass();
        verifyIsSymmetricalEquals(value, item);
        verifyIsHashCodeEqualsValue(value, item);
    }

    private void verifyNotEqualsToNull() {
        Assert.assertFalse(victim.equals(null));
    }

    private void verifyNotEqualsToDifferentClass() {
        Assert.assertFalse(victim.equals(new Object()));
    }

    private void assertItemsNotNull(Object item) {
        if (victim == null || item == null) {
            throw new AssertionError("Null does not have equals or hashcode");
        }
    }

    private void verifyIsSymmetricalEquals(boolean value, Object item) {
        Assert.assertEquals(value, victim.equals(item));
        Assert.assertEquals(value, item.equals(victim));
    }

    private void verifyIsHashCodeEqualsValue(boolean value, Object item) {
        Assert.assertEquals(value, victim.hashCode() == item.hashCode());
    }

    private void verifyDifferents() {
        if (differents == null || differents.length == 0) {
            return;
        }
        for (Object different : differents) {
            verifyEqualsValue(false, different);
        }
    }
}
