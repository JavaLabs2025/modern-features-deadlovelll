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

public class TestCreateEmployeeUseCase {

    private EmployeeRepository employeeRepository;
    private EmployeePermissionValidator validator;
    private CreateEmployeeUseCase useCase;

    @BeforeEach
    public void setUp() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        validator = new EmployeePermissionValidator(employeeRepository);
        useCase = new CreateEmployeeUseCase(employeeRepository, validator);
    }

    @Test
    public void testCreateEmployeeSuccess() {
        Employee input = new Employee();
        input.setName("Tim Cock");
        input.setAge(12);
        input.setType(EmployeeType.PROGRAMMER);

        Employee saved = new Employee();
        saved.setId(100);
        saved.setName("Tim Cock");

        Mockito.when(
                employeeRepository.create(Mockito.any(Employee.class),
                        Mockito.eq(1))
                )
                .thenReturn(saved);

        Employee creator = new Employee();
        creator.setId(1);
        creator.setType(EmployeeType.MANAGER);
        Mockito.when(employeeRepository.getById(1)).thenReturn(creator);

        Employee result = useCase.execute(input, 1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(100, result.getId());

        Mockito.verify(employeeRepository)
                .create(Mockito.any(Employee.class),
                        Mockito.eq(1)
                );
        Mockito.verify(employeeRepository).getById(1);
    }

    @Test
    public void testCreateEmployeeRaisesNotPermittedException() {
        Employee input = new Employee();
        input.setId(123);
        input.setName("Tim Cock");

        Employee creator = new Employee();
        creator.setId(1);
        creator.setType(EmployeeType.PROGRAMMER);

        Mockito.when(employeeRepository.getById(1)).thenReturn(creator);

        Assertions.assertThrows(
                NotPermittedException.class,
                () -> useCase.execute(input, 1)
        );
        Mockito.verify(employeeRepository).getById(1);
    }

    @Test
    public void testCreateEmployeeRaisesRuntimeException() {
        Employee input = new Employee();
        input.setId(123);
        input.setName("Tim Cock");

        Employee creator = new Employee();
        creator.setId(1);
        creator.setType(EmployeeType.PROGRAMMER);

        Mockito.when(employeeRepository.getById(1)).thenThrow(new RuntimeException());

        Assertions.assertThrows(
                RuntimeException.class,
                () -> useCase.execute(input, 1)
        );
        Mockito.verify(employeeRepository).getById(1);
    }
}
