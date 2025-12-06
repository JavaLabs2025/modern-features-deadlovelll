package org.lab.application.ticket.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.infra.db.repository.employee.EmployeeRepository;

public class TestTicketPermissionValidator {

    private EmployeeRepository employeeRepository;
    private TicketPermissionValidator validator;

    @BeforeEach
    void setup() {
        employeeRepository = mock(EmployeeRepository.class);
        validator = new TicketPermissionValidator(employeeRepository);
    }

    @Test
    void validate_managerAllowed() {
        Employee manager = new Employee();
        manager.setType(EmployeeType.MANAGER);
        when(employeeRepository.getById(1)).thenReturn(manager);

        validator.validate(1);
        verify(employeeRepository).getById(1);
    }

    @Test
    void validate_teamLeadAllowed() {
        Employee teamLead = new Employee();
        teamLead.setType(EmployeeType.TEAMLEAD);
        when(employeeRepository.getById(2)).thenReturn(teamLead);

        validator.validate(2);
        verify(employeeRepository).getById(2);
    }

    @Test
    void validate_notAllowed_throws() {
        Employee tester = new Employee();
        tester.setType(EmployeeType.TESTER);
        when(employeeRepository.getById(3)).thenReturn(tester);

        assertThrows(
                NotPermittedException.class,
                () -> validator.validate(3)
        );
        verify(employeeRepository).getById(3);
    }
}
