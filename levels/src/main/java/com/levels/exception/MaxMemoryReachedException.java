package com.levels.exception;

/**
 * Exception thrown when the memory limit (in the in-memory DAOs) has been
 * reached
 * 
 * @author adarrivi
 * 
 */
public class MaxMemoryReachedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MaxMemoryReachedException(String message) {
        super(message);
    }

}
