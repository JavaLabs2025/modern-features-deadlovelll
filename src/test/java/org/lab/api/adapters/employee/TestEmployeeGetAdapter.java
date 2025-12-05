package org.lab.api.adapters.employee;

import java.util.Map;

import io.javalin.http.Context;
import org.mockito.Mockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.lab.application.employee.use_cases.GetEmployeeUseCase;
import org.lab.application.shared.services.EmployeePermissionValidator;
import org.lab.application.shared.services.EmployeeProvider;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.DatabaseException;
import org.lab.infra.db.repository.employee.EmployeeRepository;

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

        Mockito.when(ctx.pathParam("actorId"))
                .thenReturn(String.valueOf(ManagerEmployee.getId()));
        Mockito.when(ctx.pathParam("employeeId"))
                .thenReturn(String.valueOf(testEmployee.getId()));
        Mockito.when(employeeRepository
                .getById(ManagerEmployee.getId()))
                .thenReturn(ManagerEmployee);
        Mockito.when(employeeRepository
                .getById(testEmployee.getId()))
                .thenReturn(testEmployee);

        Mockito.when(ctx.status(Mockito.anyInt())).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        employeeGetAdapter.getEmployee(ctx);
        Mockito.verify(ctx).status(201);
    }

    @Test
    public void testGetEmployeeHandlesNotPermittedException() {
        Employee ManagerEmployee = new Employee();
        ManagerEmployee.setId(1);
        ManagerEmployee.setName("Manager");
        ManagerEmployee.setType(EmployeeType.PROGRAMMER);

        Employee testEmployee = new Employee();
        testEmployee.setId(2);
        testEmployee.setName("test");
        testEmployee.setType(EmployeeType.TESTER);

        Mockito.when(ctx.pathParam("actorId"))
                .thenReturn(String.valueOf(ManagerEmployee.getId()));
        Mockito.when(ctx.pathParam("employeeId"))
                .thenReturn(String.valueOf(testEmployee.getId()));
        Mockito.when(employeeRepository
                .getById(ManagerEmployee.getId()))
                .thenReturn(ManagerEmployee);
        Mockito.when(employeeRepository
                .getById(testEmployee.getId()))
                .thenReturn(testEmployee);

        Mockito.when(ctx.status(Mockito.anyInt())).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        employeeGetAdapter.getEmployee(ctx);
        Mockito.verify(ctx).status(403);
        Mockito.verify(ctx).json(
                Map.of(
                        "error",
                        "You do not have permission to perform this operation"
                )
        );
    }

    @Test
    public void testGetEmployeeFetchesUnexpectedExceptionWhenFetchingActor() {
        Employee notManagerEmployee = new Employee();
        notManagerEmployee.setId(1);
        notManagerEmployee.setName("NotManager");
        notManagerEmployee.setType(EmployeeType.MANAGER);

        Employee testEmployee = new Employee();
        testEmployee.setId(2);
        testEmployee.setName("test");
        testEmployee.setType(EmployeeType.TESTER);

        Mockito.when(ctx.pathParam("actorId"))
                .thenReturn(String.valueOf(notManagerEmployee.getId()));
        Mockito.when(ctx.pathParam("employeeId"))
                .thenReturn(String.valueOf(testEmployee.getId()));

        Mockito.doThrow(DatabaseException.class)
                .when(employeeRepository)
                .getById(notManagerEmployee.getId());
        Mockito.doThrow(DatabaseException.class)
                .when(employeeRepository)
                .getById(notManagerEmployee.getId());

        Mockito.when(ctx.status(500)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        employeeGetAdapter.getEmployee(ctx);

        Mockito.verify(ctx).status(500);
        Mockito.verify(ctx).json(Map.of("error", "Internal server error"));
    }

    @Test
    public void testGetEmployeeFetchesUnexpectedExceptionWhenFetchingEmployee() {
        Employee ManagerEmployee = new Employee();
        ManagerEmployee.setId(1);
        ManagerEmployee.setName("NotManager");
        ManagerEmployee.setType(EmployeeType.MANAGER);

        Employee testEmployee = new Employee();
        testEmployee.setId(2);
        testEmployee.setName("test");
        testEmployee.setType(EmployeeType.TESTER);

        Mockito.when(ctx.pathParam("actorId"))
                .thenReturn(String.valueOf(ManagerEmployee.getId()));
        Mockito.when(ctx.pathParam("employeeId"))
                .thenReturn(String.valueOf(testEmployee.getId()));

        Mockito.when(employeeRepository
                .getById(ManagerEmployee.getId()))
                .thenReturn(ManagerEmployee);

        Mockito.doThrow(DatabaseException.class)
                .when(employeeRepository)
                .getById(testEmployee.getId());

        Mockito.when(ctx.status(500)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        employeeGetAdapter.getEmployee(ctx);

        Mockito.verify(ctx).status(500);
        Mockito.verify(ctx).json(Map.of("error", "Internal server error"));
    }
}
