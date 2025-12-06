package org.lab.application.project.use_cases;

import com.google.inject.Inject;

import org.lab.domain.project.model.Project;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.application.shared.services.EmployeePermissionValidator;

public class CreateProjectUseCase {

    private final ProjectRepository projectRepository;
    private final EmployeePermissionValidator employeePermissionValidator;

    @Inject
    public CreateProjectUseCase(
            ProjectRepository projectRepository,
            EmployeePermissionValidator employeePermissionValidator
    ) {
        this.projectRepository = projectRepository;
        this.employeePermissionValidator = employeePermissionValidator;
    }

    public Project execute(
            Project project,
            int employeeId
    ) {
        this.employeePermissionValidator.validate(employeeId);
        Project domainProject = this.projectRepository.create(project, employeeId);
        return domainProject;
    }
}
