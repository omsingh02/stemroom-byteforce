package com.stemroom.exception;

/**
 * Wraps a database error. The DAOs throw this instead of SQLException,
 * so the callers do not need a try block around every query.
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
