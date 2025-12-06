package org.lab.application.employee.use_cases;

import org.mockito.Mockito;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.lab.application.shared.services.EmployeePermissionValidator;
import org.lab.application.shared.services.EmployeeProvider;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.UserNotFoundException;
import org.lab.infra.db.repository.employee.EmployeeRepository;

public class TestGetEmployeeUseCase {

    private EmployeeRepository employeeRepository;
    private EmployeeProvider employeeProvider;
    private EmployeePermissionValidator validator;
    private GetEmployeeUseCase useCase;

    @BeforeEach
    public void setUp() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeProvider = new  EmployeeProvider(employeeRepository);
        validator = new EmployeePermissionValidator(employeeRepository);
        useCase = new GetEmployeeUseCase(validator, employeeProvider);
    }

    @Test
    public void testGetEmployeeSuccess() {
        Employee programmer = new Employee();
        programmer.setId(2);
        programmer.setType(EmployeeType.PROGRAMMER);
        Employee manager = new Employee();
        manager.setId(1);
        manager.setType(EmployeeType.MANAGER);

        Mockito.when(employeeRepository.getById(1)).thenReturn(manager);
        Mockito.when(employeeRepository.getById(2)).thenReturn(programmer);

        useCase.execute(2, 1);
        Mockito.verify(employeeRepository).getById(2);
    }

    @Test
    public void testGetEmployeeRaisesNotPermittedError() {
        Employee manager = new Employee();
        manager.setId(1);
        manager.setType(EmployeeType.PROGRAMMER);

        Mockito.when(employeeRepository.getById(1)).thenReturn(manager);

        RuntimeException thrown = Assertions.assertThrows(
                NotPermittedException.class,
                () -> useCase.execute(2, 1)
        );
    }

    @Test
    public void testGetEmployeeRaisesEmployeeNotFoundError() {
        Employee manager = new Employee();
        manager.setId(1);
        manager.setType(EmployeeType.MANAGER);

        Mockito.when(employeeRepository.getById(1)).thenReturn(manager);
        Mockito.when(employeeRepository.getById(2)).thenReturn(null);

        RuntimeException thrown = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(2, 1)
        );
    }
}
