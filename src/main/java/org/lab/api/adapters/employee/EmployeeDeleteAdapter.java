package org.lab.api.adapters.employee;

import io.javalin.http.Context;
import org.lab.application.employee.dto.DeleteEmployeeDTO;
import org.lab.application.employee.use_cases.DeleteEmployeeUseCase;
import org.lab.domain.shared.exceptions.NotPermittedException;

import java.util.Map;

public class EmployeeDeleteAdapter {

    private final DeleteEmployeeUseCase useCase = new DeleteEmployeeUseCase();

    public Context deleteEmployee(
            Context ctx
    ) {
        try {
            DeleteEmployeeDTO dto = ctx.bodyAsClass(DeleteEmployeeDTO.class);
            useCase.execute(dto.deletedEmployeeId(), dto.employeeId());
            return ctx.status(201);

        } catch (NotPermittedException e) {
            return ctx.status(403).json(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
