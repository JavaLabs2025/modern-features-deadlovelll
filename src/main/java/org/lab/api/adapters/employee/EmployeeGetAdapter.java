package org.lab.api.adapters.employee;

import java.util.Map;

import io.javalin.http.Context;

import org.lab.application.employee.dto.GetEmployeeDTO;
import org.lab.application.employee.use_cases.GetEmployeeUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;

public class EmployeeGetAdapter {

    private final GetEmployeeUseCase useCase;
    private final ObjectMapper mapper;

    public EmployeeGetAdapter(
            GetEmployeeUseCase useCase,
            ObjectMapper mapper
    ) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    public Context getEmployee(
            Context ctx
    ) {
        try {
            int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
            int actorId = Integer.parseInt(ctx.pathParam("actorId"));
            Employee receivedEmployee = useCase.execute(
                    employeeId,
                    actorId
            );
            GetEmployeeDTO presentationEmployee = mapper.mapToPresentation(
                    receivedEmployee,
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
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
