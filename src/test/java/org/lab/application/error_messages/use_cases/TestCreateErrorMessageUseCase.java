package org.lab.application.error_messages.use_cases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.lab.application.error_message.services.CreateErrorMessageValidator;
import org.lab.application.error_message.use_cases.CreateErrorMessageUseCase;
import org.lab.domain.error_mesage.model.ErrorMessage;
import org.lab.infra.db.repository.error_message.ErrorMessageRepository;
import org.lab.domain.shared.exceptions.NotPermittedException;

class CreateErrorMessageUseCaseTest {

    private ErrorMessageRepository repository;
    private CreateErrorMessageValidator validator;
    private CreateErrorMessageUseCase useCase;

    @BeforeEach
    void setup() {
        repository = mock(ErrorMessageRepository.class);
        validator = mock(CreateErrorMessageValidator.class);
        useCase = new CreateErrorMessageUseCase(repository, validator);
    }

    @Test
    void execute_success() {
        int employeeId = 10;
        ErrorMessage message = new ErrorMessage();
        message.setProjectId(1);

        ErrorMessage createdMessage = new ErrorMessage();
        when(repository.create(message, employeeId)).thenReturn(createdMessage);

        ErrorMessage result = useCase.execute(message, employeeId);

        verify(validator).validate(employeeId, message.getProjectId());
        verify(repository).create(message, employeeId);
        assertEquals(createdMessage, result);
    }

    @Test
    void execute_notPermitted() {
        int employeeId = 10;
        ErrorMessage message = new ErrorMessage();
        message.setProjectId(1);

        doThrow(new NotPermittedException("not permitted"))
                .when(validator)
                .validate(employeeId, message.getProjectId());

        assertThrows(NotPermittedException.class, () -> useCase.execute(message, employeeId));
        verify(validator).validate(employeeId, message.getProjectId());
        verifyNoInteractions(repository);
    }

    @Test
    void execute_runtimeExceptionFromRepository() {
        int employeeId = 10;
        ErrorMessage message = new ErrorMessage();
        message.setProjectId(1);

        doNothing().when(validator).validate(employeeId, message.getProjectId());
        when(repository.create(message, employeeId)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> useCase.execute(message, employeeId));
        verify(validator).validate(employeeId, message.getProjectId());
        verify(repository).create(message, employeeId);
    }
}

