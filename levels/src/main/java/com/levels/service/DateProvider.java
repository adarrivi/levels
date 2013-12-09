package com.levels.service;

import java.util.Date;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Class providing new {@link Date} instances. The main reason of this class is
 * to allow us to unit test easier functionality related with Dates
 * 
 * @author adarrivi
 * 
 */
@ThreadSafe
public class DateProvider {

    public Date getCurrentDate() {
        return new Date();
    }

}
