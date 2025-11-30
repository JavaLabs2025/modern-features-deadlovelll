package org.lab.domain.shared.exceptions;

public class NotPermittedException extends RuntimeException {
    public NotPermittedException(String message) {
        super(message);
    }
}
