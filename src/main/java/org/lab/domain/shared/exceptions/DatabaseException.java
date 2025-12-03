package org.lab.domain.shared.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException() {
        super("Something went wrong with database operation");
    }
}
