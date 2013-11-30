package com.levels.service;

import java.util.Date;

/**
 * Singleton class providing new {@link Date} instances. The main reason of this
 * class is to allow us to mock easier Dates in Unit testing
 * 
 * @author adarrivi
 * 
 */
public class DateProvider {

    private static final DateProvider INSTANCE = new DateProvider();

    private DateProvider() {
        // Limiting scope.
    }

    public static DateProvider getInstance() {
        return INSTANCE;
    }

    public Date getCurrentDate() {
        return new Date();
    }

}
