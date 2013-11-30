package com.levels.exception;

public class MaxMemoryReachedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MaxMemoryReachedException(String message) {
        super(message);
    }

}
