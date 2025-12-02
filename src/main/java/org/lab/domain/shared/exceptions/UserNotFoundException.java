package org.lab.domain.shared.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Employee not found");
    }
}
