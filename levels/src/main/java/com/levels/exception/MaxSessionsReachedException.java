package com.levels.exception;

public class MaxSessionsReachedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MaxSessionsReachedException(String message) {
        super(message);
    }

}
