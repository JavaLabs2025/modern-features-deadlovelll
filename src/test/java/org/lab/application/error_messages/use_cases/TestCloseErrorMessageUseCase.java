package org.lab.application.error_messages.use_cases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.lab.application.error_message.use_cases.CloseErrorMessageUseCase;
import org.lab.domain.error_mesage.model.ErrorMessage;
import org.lab.domain.shared.exceptions.MessageNotFoundException;
import org.lab.application.error_message.services.ErrorMessageValidator;
import org.lab.infra.db.repository.error_message.ErrorMessageRepository;

class TestCloseErrorMessageUseCase {

    private ErrorMessageValidator validator;
    private ErrorMessageRepository repository;
    private CloseErrorMessageUseCase useCase;

    @BeforeEach
    void setup() {
        validator = mock(ErrorMessageValidator.class);
        repository = mock(ErrorMessageRepository.class);
        useCase = new CloseErrorMessageUseCase(validator, repository);
    }

    @Test
    void execute_success() {
        int messageId = 1;
        ErrorMessage closedMessage = new ErrorMessage();
        when(repository.close(messageId)).thenReturn(closedMessage);

        ErrorMessage result = useCase.execute(messageId);

        verify(validator).validate(messageId);
        verify(repository).close(messageId);
        assertEquals(closedMessage, result);
    }

    @Test
    void execute_messageNotFound() {
        int messageId = 2;
        doThrow(new MessageNotFoundException()).when(validator).validate(messageId);

        assertThrows(MessageNotFoundException.class, () -> useCase.execute(messageId));

        verify(validator).validate(messageId);
        verifyNoInteractions(repository);
    }
}

