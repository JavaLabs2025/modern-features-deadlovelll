package org.lab.application.ticket.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.lab.application.shared.services.ProjectProvider;
import org.lab.domain.shared.exceptions.NotPermittedException;

public class TestTicketCreateValidator {

    private TicketPermissionValidator ticketPermissionValidator;
    private ProjectProvider projectProvider;
    private TicketCreateValidator validator;

    @BeforeEach
    void setup() {
        ticketPermissionValidator = mock(TicketPermissionValidator.class);
        projectProvider = mock(ProjectProvider.class);
        validator = new TicketCreateValidator(ticketPermissionValidator, projectProvider);
    }

    @Test
    void validate_success_noException() {
        int employeeId = 10;
        int projectId = 5;

        validator.validate(employeeId, projectId);
        verify(ticketPermissionValidator).validate(employeeId);
        verify(projectProvider).get(projectId);
    }

    @Test
    void validate_ticketPermissionThrows_exception() {
        int employeeId = 10;
        int projectId = 5;

        doThrow(new NotPermittedException("not allowed"))
                .when(ticketPermissionValidator)
                .validate(employeeId);

        assertThrows(
                RuntimeException.class,
                () -> validator.validate(employeeId, projectId)
        );

        verify(ticketPermissionValidator).validate(employeeId);
    }

    @Test
    void validate_projectProviderThrows_exception() {
        int employeeId = 10;
        int projectId = 5;

        doThrow(new RuntimeException("project not found"))
                .when(projectProvider)
                .get(projectId);

        assertThrows(
                RuntimeException.class,
                () -> validator.validate(employeeId, projectId)
        );

        verify(ticketPermissionValidator).validate(employeeId);
        verify(projectProvider).get(projectId);
    }
}
