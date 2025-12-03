package org.lab.api.adapters.employee;

import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.application.employee.use_cases.DeleteEmployeeUseCase;
import org.lab.application.employee.use_cases.GetEmployeeUseCase;
import org.lab.application.shared.services.EmployeePermissionValidator;
import org.lab.application.shared.services.EmployeeProvider;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.repository.employee.EmployeeRepository;
import org.mockito.Mockito;

public class TestEmployeeGetAdapter {
    private GetEmployeeUseCase getEmployeeUseCase;
    private EmployeeRepository employeeRepository;
    private EmployeePermissionValidator employeePermissionValidator;
    private EmployeeGetAdapter employeeGetAdapter;
    private EmployeeProvider employeeProvider;
    private Context ctx;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        ctx = Mockito.mock(Context.class);
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeePermissionValidator = new EmployeePermissionValidator(
                employeeRepository
        );
        employeeProvider =  new EmployeeProvider(employeeRepository);
        getEmployeeUseCase = new GetEmployeeUseCase(
                employeePermissionValidator,
                employeeProvider
        );
        mapper = new ObjectMapper();
        employeeGetAdapter = new  EmployeeGetAdapter(
                getEmployeeUseCase,
                mapper
        );
    }

    @Test
    public void testGetEmployeeSuccess() {
        Employee ManagerEmployee = new Employee();
        ManagerEmployee.setId(1);
        ManagerEmployee.setName("Manager");
        ManagerEmployee.setType(EmployeeType.MANAGER);

        Employee testEmployee = new Employee();
        testEmployee.setId(2);
        testEmployee.setName("test");
        testEmployee.setType(EmployeeType.TESTER);

        Mockito.when(ctx.pathParam("actorId")).thenReturn(String.valueOf(ManagerEmployee.getId()));
        Mockito.when(ctx.pathParam("employeeId")).thenReturn(String.valueOf(testEmployee.getId()));
        Mockito.when(employeeRepository.getById(ManagerEmployee.getId())).thenReturn(ManagerEmployee);
        Mockito.when(employeeRepository.getById(testEmployee.getId())).thenReturn(testEmployee);

        Mockito.when(ctx.status(Mockito.anyInt())).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        employeeGetAdapter.getEmployee(ctx);
        Mockito.verify(ctx).status(201);
    }
}
