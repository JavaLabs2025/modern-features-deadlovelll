package org.lab.api.adapters.employee;

import io.javalin.http.Context;

import org.lab.application.employee.dto.GetEmployeeDTO;
import org.lab.application.employee.dto.CreateEmployeeDTO;
import org.lab.application.employee.use_cases.CreateEmployeeUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.domain.shared.exceptions.UserAlreadyExistsException;

import java.util.Map;

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
            CreateEmployeeDTO dto = ctx.bodyAsClass(CreateEmployeeDTO.class);
            Employee employee = mapper.mapToDomain(dto, Employee.class);
            Employee createdEmployee = useCase.execute(
                    employee,
                    dto.creatorId()
            );
            GetEmployeeDTO presentationEmployee = mapper.mapToPresentation(
                    createdEmployee,
                    GetEmployeeDTO.class
            );
            return ctx.status(201).json(presentationEmployee);

        } catch (UserAlreadyExistsException e) {
            return ctx.status(409).json(Map.of("error", "User already exists"));

        } catch (NotPermittedException e) {
            return ctx.status(403).json(
                    Map.of(
                            "error",
                            "You do not have permission to perform this operation"
                    )
            );

        } catch (Exception e) {
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
