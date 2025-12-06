package org.lab.application.project.services;

import java.util.concurrent.*;

import org.lab.application.shared.services.ProjectProvider;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.shared.exceptions.UserNotFoundException;
import org.lab.application.shared.services.EmployeeProvider;

public class GetValidator {

    private final ProjectProvider projectProvider;
    private final EmployeeProvider currentEmployeeProvider;

    public GetValidator(
            ProjectProvider projectProvider,
            EmployeeProvider currentEmployeeProvider
    ) {
        this.projectProvider = projectProvider;
        this.currentEmployeeProvider = currentEmployeeProvider;
    }

    public Pair validate(
            int projectId,
            int employeeId
    )
            throws ProjectNotFoundException,
            UserNotFoundException
    {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()){
            var projectFuture = scope.fork(() -> {
                Project project = this.projectProvider.get(projectId);
                return project;
            });
            var employeeFuture = scope.fork(() -> {
                Employee employee = this.currentEmployeeProvider.get(employeeId);
                return employee;
            });
            scope.join();
            scope.throwIfFailed();

            Project project = projectFuture.get();
            Employee employee = employeeFuture.get();
            return new Pair(project, employee);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}