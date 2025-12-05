package org.lab.api.adapters.employee;

import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.application.employee.dto.CreateEmployeeDTO;
import org.lab.application.employee.dto.GetEmployeeDTO;
import org.lab.application.employee.use_cases.CreateEmployeeUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;

import org.mockito.Mockito;

import java.util.Date;
import java.util.Map;

public class TestEmployeeCreateAdapter {

    private ObjectMapper mapper;
    private CreateEmployeeUseCase useCase;
    private EmployeeCreateAdapter adapter;
    private Context ctx;

    @BeforeEach
    public void setUp() {
        mapper = Mockito.mock(ObjectMapper.class);
        useCase = Mockito.mock(CreateEmployeeUseCase.class);
        ctx = Mockito.mock(Context.class);
        adapter = new EmployeeCreateAdapter(mapper, useCase);
    }

    @Test
    public void testCreateEmployeeSuccess() {
        CreateEmployeeDTO dto = new CreateEmployeeDTO(
                "John",
                30,
                EmployeeType.PROGRAMMER,
                99
        );

        Employee mappedEmployee = new Employee();
        mappedEmployee.setName("John");
        mappedEmployee.setAge(30);
        mappedEmployee.setType(EmployeeType.PROGRAMMER);

        Employee createdEmployee = new Employee();
        createdEmployee.setId(5);
        createdEmployee.setName("John");
        createdEmployee.setAge(30);
        createdEmployee.setType(EmployeeType.PROGRAMMER);
        createdEmployee.setCreatedBy(99);
        createdEmployee.setCreatedDate(new Date());

        GetEmployeeDTO presentation = new GetEmployeeDTO(
                createdEmployee.getId(),
                createdEmployee.getName(),
                createdEmployee.getAge(),
                createdEmployee.getType(),
                createdEmployee.getCreatedBy(),
                createdEmployee.getCreatedDate()
        );

        Mockito.when(ctx.bodyAsClass(CreateEmployeeDTO.class))
                .thenReturn(dto);

        Mockito.when(mapper.mapToDomain(dto, Employee.class))
                .thenReturn(mappedEmployee);

        Mockito.when(useCase.execute(mappedEmployee, dto.creatorId()))
                .thenReturn(createdEmployee);

        Mockito.when(mapper.mapToPresentation(createdEmployee, GetEmployeeDTO.class))
                .thenReturn(presentation);

        Mockito.when(ctx.status(201)).thenReturn(ctx);
        Mockito.when(ctx.json(presentation)).thenReturn(ctx);

        adapter.createEmployee(ctx);

        Mockito.verify(ctx).status(201);
        Mockito.verify(ctx).json(presentation);
    }

    @Test
    public void testCreateEmployeeNotPermitted() {
        CreateEmployeeDTO dto = new CreateEmployeeDTO(
                "John",
                30,
                EmployeeType.PROGRAMMER,
                99
        );

        Mockito.when(ctx.bodyAsClass(CreateEmployeeDTO.class)).thenReturn(dto);
        Mockito.when(mapper.mapToDomain(dto, Employee.class))
                .thenReturn(new Employee());

        Mockito.when(useCase.execute(Mockito.any(), Mockito.eq(dto.creatorId())))
                .thenThrow(new NotPermittedException(
                        "You do not have permission to perform this operation"
                ));

        Mockito.when(ctx.status(403)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.createEmployee(ctx);

        Mockito.verify(ctx).status(403);
        Mockito.verify(ctx).json(
                Map.of("error", "You do not have permission to perform this operation")
        );
    }

    @Test
    public void testCreateEmployeeUnexpectedError() {
        CreateEmployeeDTO dto = new CreateEmployeeDTO(
                "John",
                30,
                EmployeeType.PROGRAMMER,
                99
        );

        Mockito.when(ctx.bodyAsClass(CreateEmployeeDTO.class)).thenReturn(dto);

        Mockito.when(mapper.mapToDomain(dto, Employee.class))
                .thenThrow(new RuntimeException("boom"));

        Mockito.when(ctx.status(500)).thenReturn(ctx);
        Mockito.when(ctx.json(Mockito.any())).thenReturn(ctx);

        adapter.createEmployee(ctx);

        Mockito.verify(ctx).status(500);
        Mockito.verify(ctx).json(Map.of("error", "Internal server error"));
    }
}
