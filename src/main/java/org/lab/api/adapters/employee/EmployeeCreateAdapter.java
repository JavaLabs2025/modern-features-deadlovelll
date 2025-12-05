package org.lab.api.adapters.employee;

import java.util.Map;

import io.javalin.http.Context;

import org.lab.application.employee.dto.GetEmployeeDTO;
import org.lab.application.employee.dto.CreateEmployeeDTO;
import org.lab.application.employee.use_cases.CreateEmployeeUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;

public class EmployeeCreateAdapter {

    private final ObjectMapper mapper;
    private final CreateEmployeeUseCase useCase;

    public EmployeeCreateAdapter(
            ObjectMapper mapper,
            CreateEmployeeUseCase useCase
    ) {
        this.mapper = mapper;
        this.useCase = useCase;
    }

    public Context createEmployee(
            Context ctx
    ) {
        try {
            int actorId = Integer.parseInt(ctx.pathParam("actorId"));
            CreateEmployeeDTO dto = ctx.bodyAsClass(CreateEmployeeDTO.class);
            Employee employee = mapper.mapToDomain(dto, Employee.class);
            Employee createdEmployee = useCase.execute(
                    employee,
                    actorId
            );
            GetEmployeeDTO presentationEmployee = mapper.mapToPresentation(
                    createdEmployee,
                    GetEmployeeDTO.class
            );
            return ctx.status(201).json(presentationEmployee);

        } catch (NotPermittedException e) {
            return ctx.status(403).json(
                    Map.of(
                            "error",
                            "You do not have permission to perform this operation"
                    )
            );

        } catch (Exception e) {
            System.err.println("ERROR" + e.getMessage());
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
