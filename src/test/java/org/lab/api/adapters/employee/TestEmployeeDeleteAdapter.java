package org.lab.api.adapters.employee;

import java.util.Map;

import io.javalin.http.Context;
import org.mockito.Mockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.lab.application.employee.use_cases.DeleteEmployeeUseCase;
import org.lab.application.shared.services.EmployeePermissionValidator;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.repository.employee.EmployeeRepository;
import org.lab.domain.shared.exceptions.DatabaseException;

public class TestEmployeeDeleteAdapter {
    private DeleteEmployeeUseCase deleteEmployeeUseCase;
    private EmployeeRepository employeeRepository;
    private EmployeePermissionValidator employeePermissionValidator;
    private EmployeeDeleteAdapter employeeDeleteAdapter;
    private Context ctx;

    @BeforeEach
    public void setUp() {
        ctx = Mockito.mock(Context.class);
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeePermissionValidator = new EmployeePermissionValidator(
                employeeRepository
        );
        deleteEmployeeUseCase = new DeleteEmployeeUseCase(
                employeeRepository,
                employeePermissionValidator
        );
        employeeDeleteAdapter = new  EmployeeDeleteAdapter(deleteEmployeeUseCase);
    }

    @Test
    public void testDeleteEmployeeFetchesNotPermittedException() {
        Employee notManagerEmployee = new Employee();
        notManagerEmployee.setId(1);
        notManagerEmployee.setName("NotManager");
        notManagerEmployee.setType(EmployeeType.PROGRAMMER);

        Employee testEmployee = new Employee();
        testEmployee.setId(2);
        testEmployee.setName("test");
        testEmployee.setType(EmployeeType.TESTER);

        Mockito.when(ctx.pathParam("actorId")).thenReturn(String.valueOf(notManagerEmployee.getId()));
        Mockito.when(ctx.pathParam("employeeId")).thenReturn(String.valueOf(testEmployee.getId()));
        Mockito.when(employeeRepository.getById(notManagerEmployee.getId())).thenReturn(notManagerEmployee);

        Mockito.when(ctx.status(403)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        employeeDeleteAdapter.deleteEmployee(ctx);

        Mockito.verify(ctx).status(403);
        Mockito.verify(ctx).json(Map.of("error", "You do not have permission to perform this operation"));
    }

    @Test
    public void testDeleteEmployeeFetchesUnexpectedExceptionWhenFetchingEmployee() {
        Employee notManagerEmployee = new Employee();
        notManagerEmployee.setId(1);
        notManagerEmployee.setName("NotManager");
        notManagerEmployee.setType(EmployeeType.PROGRAMMER);

        Employee testEmployee = new Employee();
        testEmployee.setId(2);
        testEmployee.setName("test");
        testEmployee.setType(EmployeeType.TESTER);

        Mockito.when(ctx.pathParam("actorId")).thenReturn(String.valueOf(notManagerEmployee.getId()));
        Mockito.when(ctx.pathParam("employeeId")).thenReturn(String.valueOf(testEmployee.getId()));
        Mockito.doThrow(DatabaseException.class).when(employeeRepository).getById(notManagerEmployee.getId());

        Mockito.when(ctx.status(500)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        employeeDeleteAdapter.deleteEmployee(ctx);

        Mockito.verify(ctx).status(500);
        Mockito.verify(ctx).json(Map.of("error", "Internal server error"));
    }

    @Test
    public void testDeleteEmployeeFetchesUnexpectedExceptionWhenDeletingEmployee() {
        Employee notManagerEmployee = new Employee();
        notManagerEmployee.setId(1);
        notManagerEmployee.setName("NotManager");
        notManagerEmployee.setType(EmployeeType.PROGRAMMER);

        Employee testEmployee = new Employee();
        testEmployee.setId(2);
        testEmployee.setName("test");
        testEmployee.setType(EmployeeType.TESTER);

        Mockito.when(ctx.pathParam("actorId")).thenReturn(String.valueOf(notManagerEmployee.getId()));
        Mockito.when(ctx.pathParam("employeeId")).thenReturn(String.valueOf(testEmployee.getId()));
        Mockito.doThrow(DatabaseException.class).when(employeeRepository).delete(testEmployee.getId());

        Mockito.when(ctx.status(500)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        employeeDeleteAdapter.deleteEmployee(ctx);

        Mockito.verify(ctx).status(500);
        Mockito.verify(ctx).json(Map.of("error", "Internal server error"));
    }
}
