package org.lab.api.adapters.employee;

import io.javalin.http.Context;
import org.lab.application.employee.dto.GetEmployeeDTOIn;
import org.lab.application.employee.dto.GetEmployeeDTOOut;
import org.lab.application.employee.use_cases.GetEmployeeUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;

import java.util.Map;

public class EmployeeGetAdapter {

    private final GetEmployeeUseCase useCase = new GetEmployeeUseCase();
    private final ObjectMapper mapper = new ObjectMapper();

    public Context getEmployee(
            Context ctx
    ) {
        try {
            GetEmployeeDTOIn dto = new GetEmployeeDTOIn(
                    Integer.parseInt(ctx.queryParam("employeeId")),
                    Integer.parseInt(ctx.queryParam("actorId"))
            );
            Employee receivedEmployee = useCase.execute(
                    dto.employeeId(),
                    dto.actorId()
            );
            GetEmployeeDTOOut presentationEmployee = mapper.mapToPresentation(
                    receivedEmployee,
                    GetEmployeeDTOOut.class
            );
            return ctx.status(201).json(presentationEmployee);

        } catch (NotPermittedException e) {
            return ctx.status(403).json(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }
}
