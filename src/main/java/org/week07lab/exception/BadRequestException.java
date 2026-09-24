package org.week07lab.exception;

/**
 * El request viola una regla de negocio (400).
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
