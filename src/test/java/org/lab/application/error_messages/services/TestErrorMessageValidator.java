package org.lab.application.error_messages.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.lab.application.error_message.services.ErrorMessageValidator;
import org.lab.domain.error_mesage.model.ErrorMessage;
import org.lab.domain.shared.exceptions.MessageNotFoundException;
import org.lab.infra.db.repository.error_message.ErrorMessageRepository;

class TestErrorMessageValidator {

    private ErrorMessageRepository repository;
    private ErrorMessageValidator validator;

    @BeforeEach
    void setup() {
        repository = mock(ErrorMessageRepository.class);
        validator = new ErrorMessageValidator(repository);
    }

    @Test
    void validate_messageExists() {
        ErrorMessage message = new ErrorMessage();
        when(repository.get(1)).thenReturn(message);

        assertDoesNotThrow(() -> validator.validate(1));

        verify(repository).get(1);
    }

    @Test
    void validate_messageNotFound() {
        when(repository.get(2)).thenReturn(null);

        assertThrows(MessageNotFoundException.class, () -> validator.validate(2));

        verify(repository).get(2);
    }
}

