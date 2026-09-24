package org.week07lab.exception;

/**
 * El request entra en conflicto con el estado actual (409).
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
