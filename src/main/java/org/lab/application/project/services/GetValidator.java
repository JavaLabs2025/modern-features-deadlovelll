package org.lab.application.project.services;

import java.util.concurrent.*;

import org.lab.domain.emploee.model.Employee;
import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.ProjectNotFoundException;
import org.lab.domain.shared.exceptions.UserNotFoundException;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.application.shared.services.CurrentEmployeeProvider;

public class GetValidator {

    private final ProjectRepository projectRepository;
    private final CurrentEmployeeProvider currentEmployeeProvider;

    public GetValidator(
            ProjectRepository projectRepository,
            CurrentEmployeeProvider currentEmployeeProvider
    ) {
        this.projectRepository = projectRepository;
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
                Project project = this.projectRepository.get(projectId);
                if (project == null) {
                    throw new ProjectNotFoundException();
                }
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