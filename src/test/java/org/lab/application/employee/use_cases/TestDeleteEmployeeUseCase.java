package org.lab.application.employee.use_cases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.lab.application.shared.services.EmployeePermissionValidator;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.repository.employee.EmployeeRepository;

public class TestDeleteEmployeeUseCase {

    private EmployeeRepository employeeRepository;
    private EmployeePermissionValidator validator;
    private DeleteEmployeeUseCase useCase;

    @BeforeEach
    public void setUp() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        validator = new EmployeePermissionValidator(employeeRepository);
        useCase = new DeleteEmployeeUseCase(employeeRepository, validator);
    }

    @Test
    public void testDeleteEmployeeSuccess() {
        Employee manager = new Employee();
        manager.setId(1);
        manager.setType(EmployeeType.MANAGER);

        Mockito.when(employeeRepository.getById(1)).thenReturn(manager);

        useCase.execute(2, 1);
        Mockito.verify(employeeRepository).getById(1);
    }

    @Test
    public void testDeleteEmployeeRaisesNotPermittedError() {
        Employee manager = new Employee();
        manager.setId(1);
        manager.setType(EmployeeType.PROGRAMMER);

        Mockito.when(employeeRepository.getById(1)).thenReturn(manager);

        Assertions.assertThrows(
                NotPermittedException.class,
                () -> useCase.execute(2, 1)
        );
    }
}
