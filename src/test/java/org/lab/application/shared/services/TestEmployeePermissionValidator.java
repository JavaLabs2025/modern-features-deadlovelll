package org.lab.application.shared.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.infra.db.repository.employee.EmployeeRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TestEmployeePermissionValidator {

    private EmployeeRepository employeeRepository;
    private EmployeePermissionValidator validator;

    @BeforeEach
    void setup() {
        employeeRepository = mock(EmployeeRepository.class);
        validator = new EmployeePermissionValidator(employeeRepository);
    }

    @Test
    void validate_manager_passes() {
        Employee manager = new Employee();
        manager.setId(1);
        manager.setType(EmployeeType.MANAGER);

        when(employeeRepository.getById(1)).thenReturn(manager);

        validator.validate(1);
        verify(employeeRepository).getById(1);
    }

    @Test
    void validate_nonManager_throwsException() {
        Employee programmer = new Employee();
        programmer.setId(2);
        programmer.setType(EmployeeType.PROGRAMMER);

        when(employeeRepository.getById(2)).thenReturn(programmer);

        assertThrows(NotPermittedException.class, () -> validator.validate(2));
        verify(employeeRepository).getById(2);
    }

    @Test
    void validate_nullEmployee_throwsException() {
        when(employeeRepository.getById(3)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> validator.validate(3));
        verify(employeeRepository).getById(3);
    }
}

