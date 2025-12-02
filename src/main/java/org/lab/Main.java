package org.lab;

import io.javalin.Javalin;
import org.lab.api.adapters.employee.EmployeeCreateAdapter;
import org.lab.api.adapters.employee.EmployeeDeleteAdapter;
import org.lab.api.adapters.employee.EmployeeGetAdapter;
import org.lab.application.employee.services.CreateValidator;
import org.lab.application.employee.services.EmployeePermissionValidator;
import org.lab.application.employee.use_cases.CreateEmployeeUseCase;
import org.lab.application.employee.use_cases.DeleteEmployeeUseCase;
import org.lab.application.employee.use_cases.GetEmployeeUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.infra.db.repository.employee.EmployeeRepository;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {}).start(7070);

        EmployeeCreateAdapter createEmployeeAdapter = new EmployeeCreateAdapter(
                new ObjectMapper(),
                new CreateEmployeeUseCase(
                        new EmployeeRepository(),
                        new CreateValidator(
                                new EmployeePermissionValidator(
                                        new EmployeeRepository()
                                ),
                                new EmployeeRepository()
                        )
                )
        );
        EmployeeDeleteAdapter deleteEmployeeAdapter = new EmployeeDeleteAdapter(
                new DeleteEmployeeUseCase(
                        new EmployeeRepository(),
                        new EmployeePermissionValidator(
                                new EmployeeRepository()
                        )
                )
        );
        EmployeeGetAdapter getEmployeeAdapter = new EmployeeGetAdapter(
                new GetEmployeeUseCase(
                        new EmployeeRepository(),
                        new EmployeePermissionValidator(
                                new EmployeeRepository()
                        )
                ),
                new ObjectMapper()
        );

        app.get("/", ctx -> ctx.result("Hello World"));

        app.post("/employee", createEmployeeAdapter::createEmployee);
        app.delete("/employee", deleteEmployeeAdapter::deleteEmployee);
        app.get("/employee", getEmployeeAdapter::getEmployee);
    }
}
