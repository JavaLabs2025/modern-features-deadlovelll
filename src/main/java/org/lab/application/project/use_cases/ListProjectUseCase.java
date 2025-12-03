package org.lab.application.project.use_cases;

import java.util.List;

import org.lab.application.project.services.UserSpecFactory;
import org.lab.application.shared.services.CurrentEmployeeProvider;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.project.model.Project;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.infra.db.spec.Specification;

public class ListProjectUseCase {

    private final ProjectRepository projectRepository;
    private final CurrentEmployeeProvider currentEmployeeProvider;
    private final UserSpecFactory userSpecFactory;

    public ListProjectUseCase(
            ProjectRepository projectRepository,
            CurrentEmployeeProvider currentEmployeeProvider,
            UserSpecFactory userSpecFactory
    ) {
        this.projectRepository = projectRepository;
        this.currentEmployeeProvider = currentEmployeeProvider;
        this.userSpecFactory = userSpecFactory;
    }

    public List<Project> execute(
            int employeeId
    ) {
        Employee employee = this.currentEmployeeProvider.get(employeeId);
        Specification spec = this.userSpecFactory.getForType(employee);
        List<Project> projects = this.projectRepository.list(spec);
        return projects;
    }
}
