package com.levels.http.server;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.levels.exception.InvalidParameterException;
import com.levels.http.server.ParameterVerifier;

public class ParameterVerifierTest {

    private ParameterVerifier victim = new ParameterVerifier();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // input parameters
    private String value;

    // output parameters
    private int intValue;

    @Test
    public void getValueAsUnsignedInt_Null_ThrowsEx() {
        expectedException.expect(InvalidParameterException.class);
        givenValue(null);
        whenGetValueAsUnsignedInt();
    }

    private void givenValue(String aValue) {
        value = aValue;

    }

    private void whenGetValueAsUnsignedInt() {
        intValue = victim.getValueAsUnsignedInt(value);
    }

    @Test
    public void getValueAsUnsignedInt_Empty_ThrowsEx() {
        expectedException.expect(InvalidParameterException.class);
        givenValue("");
        whenGetValueAsUnsignedInt();
    }

    @Test
    public void getValueAsUnsignedInt_NotAnInt_ThrowsEx() {
        expectedException.expect(InvalidParameterException.class);
        givenValue("abcd");
        whenGetValueAsUnsignedInt();
    }

    @Test
    public void getValueAsUnsignedInt_Negative_ThrowsEx() {
        expectedException.expect(InvalidParameterException.class);
        givenValue("-2345");
        whenGetValueAsUnsignedInt();
    }

    @Test
    public void getValueAsUnsignedInt_BiggerThan31bitInteger_ThrowsEx() {
        expectedException.expect(InvalidParameterException.class);
        long longValue = Long.valueOf(Integer.MAX_VALUE) + 1;
        givenValue(Long.toString(longValue));
        whenGetValueAsUnsignedInt();
    }

    @Test
    public void getValueAsUnsignedInt_MaxIntegerValue_ReturnsMaxValue() {
        givenValue(Integer.toString(Integer.MAX_VALUE));
        whenGetValueAsUnsignedInt();
        thenIntValueShouldBe(Integer.MAX_VALUE);
    }

    @Test
    public void getValueAsUnsignedInt_Zero_ReturnsZero() {
        givenValue("0");
        whenGetValueAsUnsignedInt();
        thenIntValueShouldBe(0);
    }

    private void thenIntValueShouldBe(int expectedValue) {
        Assert.assertTrue(expectedValue == intValue);
    }

}
