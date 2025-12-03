package org.lab.api.adapters.employee;

import io.javalin.http.Context;
import org.lab.application.employee.use_cases.DeleteEmployeeUseCase;
import org.lab.domain.shared.exceptions.NotPermittedException;

import java.util.Map;

public class EmployeeDeleteAdapter {

    private final DeleteEmployeeUseCase useCase;

    public EmployeeDeleteAdapter(
            DeleteEmployeeUseCase useCase
    ) {
        this.useCase = useCase;
    }

    public Context deleteEmployee(
            Context ctx
    ) {
        try {
            int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
            int actorId = Integer.parseInt(ctx.pathParam("actorId"));
            this.useCase.execute(employeeId, actorId);
            return ctx.status(201);

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
