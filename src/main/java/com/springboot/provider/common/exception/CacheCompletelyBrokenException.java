package com.springboot.provider.common.exception;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.exception
 * @Author xuzhenkui
 * @Date 2021-05-2516:37
 */
public class CacheCompletelyBrokenException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CacheCompletelyBrokenException(String message) {
        super(message);
    }
}
