package org.lab.application.project.use_cases;

import org.lab.domain.emploee.model.Employee;
import org.lab.domain.project.model.Project;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.application.project.services.GetValidator;
import org.lab.application.project.services.Pair;
import org.lab.domain.project.services.ProjectMembershipValidator;

public class GetProjectUseCase {

    private final ProjectRepository projectRepository;
    private final GetValidator getValidator;
    private final ProjectMembershipValidator projectMembershipValidator;

    public GetProjectUseCase(
            ProjectRepository projectRepository,
            GetValidator getValidator,
            ProjectMembershipValidator projectMembershipValidator
    ) {
        this.projectRepository = projectRepository;
        this.getValidator = getValidator;
        this.projectMembershipValidator = projectMembershipValidator;
    }

    public Project execute(
            int projectId,
            int employeeId
    ) {
        Pair pair = this.getValidator.validate(projectId, employeeId);
        Project project = pair.project();
        Employee employee = pair.employee();
        this.projectMembershipValidator.validate(employee, project);
        return project;
    }
}
