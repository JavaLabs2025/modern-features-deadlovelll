package org.lab;

import io.javalin.Javalin;
import org.lab.api.adapters.employee.EmployeeCreateAdapter;
import org.lab.api.adapters.employee.EmployeeDeleteAdapter;
import org.lab.api.adapters.employee.EmployeeGetAdapter;
import org.lab.api.adapters.project.ProjectCreateAdapter;
import org.lab.api.adapters.project.ProjectDeleteAdapter;
import org.lab.api.adapters.project.ProjectGetAdapter;
import org.lab.api.adapters.project.ProjectListAdapter;
import org.lab.application.employee.services.CreateValidator;
import org.lab.application.project.services.GetValidator;
import org.lab.application.project.services.UserSpecFactory;
import org.lab.application.project.use_cases.CreateProjectUseCase;
import org.lab.application.project.use_cases.DeleteProjectUseCase;
import org.lab.application.project.use_cases.GetProjectUseCase;
import org.lab.application.project.use_cases.ListProjectUseCase;
import org.lab.application.shared.services.CurrentEmployeeProvider;
import org.lab.application.shared.services.EmployeePermissionValidator;
import org.lab.application.employee.use_cases.CreateEmployeeUseCase;
import org.lab.application.employee.use_cases.DeleteEmployeeUseCase;
import org.lab.application.employee.use_cases.GetEmployeeUseCase;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.project.services.ProjectMembershipValidator;
import org.lab.infra.db.repository.employee.EmployeeRepository;
import org.lab.infra.db.repository.project.ProjectRepository;

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

        ProjectCreateAdapter createProjectAdapter = new ProjectCreateAdapter(
                new ObjectMapper(),
                new CreateProjectUseCase(
                        new ProjectRepository(),
                        new EmployeePermissionValidator(
                                new EmployeeRepository()
                        )
                )
        );
        ProjectGetAdapter projectGetAdapter = new ProjectGetAdapter(
                new GetProjectUseCase(
                        new GetValidator(
                                new ProjectRepository(),
                                new CurrentEmployeeProvider(
                                        new EmployeeRepository()
                                )
                        ),
                        new ProjectMembershipValidator()
                ),
                new ObjectMapper()
        );

        ProjectDeleteAdapter projectDeleteAdapter = new ProjectDeleteAdapter(
                new DeleteProjectUseCase(
                        new ProjectRepository(),
                        new GetValidator(
                                new ProjectRepository(),
                                new CurrentEmployeeProvider(
                                        new EmployeeRepository()
                                )
                        ),
                        new ProjectMembershipValidator()
                )
        );

        ProjectListAdapter projectListAdapter = new ProjectListAdapter(
                new ListProjectUseCase(
                        new ProjectRepository(),
                        new CurrentEmployeeProvider(
                                new EmployeeRepository()
                        ),
                        new UserSpecFactory()
                ),
                new ObjectMapper()
        );

        app.get("/", ctx -> ctx.result("Hello World"));

        app.post("/employee", createEmployeeAdapter::createEmployee);
        app.delete("/employee", deleteEmployeeAdapter::deleteEmployee);
        app.get("/employee", getEmployeeAdapter::getEmployee);

        app.post("/project", createProjectAdapter::createProject);
        app.get("/project", projectGetAdapter::getProject);
        app.delete("/project", projectDeleteAdapter::deleteProject);
        app.get("/project/{employeeId}", projectListAdapter::listProjects);
    }
}
