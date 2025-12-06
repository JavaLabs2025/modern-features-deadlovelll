package org.lab.domain.shared.exceptions;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException() {
        super("Error message not found");
    }
}
