package org.lab.application.shared.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.UserNotFoundException;
import org.lab.infra.db.repository.employee.EmployeeRepository;

class TestEmployeeProvider {

    private EmployeeRepository repository;
    private EmployeeProvider provider;

    @BeforeEach
    void setup() {
        repository = mock(EmployeeRepository.class);
        provider = new EmployeeProvider(repository);
    }

    @Test
    void get_existingEmployee_returnsEmployee() throws UserNotFoundException {
        int employeeId = 1;
        Employee employee = new Employee();
        employee.setId(employeeId);

        when(repository.getById(employeeId)).thenReturn(employee);

        Employee result = provider.get(employeeId);

        assertEquals(employee, result);
        verify(repository).getById(employeeId);
    }

    @Test
    void get_nonExistingEmployee_throwsException() {
        int employeeId = 1;

        when(repository.getById(employeeId)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> provider.get(employeeId));
        verify(repository).getById(employeeId);
    }
}
