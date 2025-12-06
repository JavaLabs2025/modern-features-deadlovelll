package org.lab.application.project.use_cases;

import java.util.List;

import com.google.inject.Inject;
import org.lab.application.project.services.UserSpecFactory;
import org.lab.application.shared.services.EmployeeProvider;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.project.model.Project;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.infra.db.spec.Specification;

public class ListProjectUseCase {

    private final ProjectRepository projectRepository;
    private final EmployeeProvider currentEmployeeProvider;
    private final UserSpecFactory userSpecFactory;

    @Inject
    public ListProjectUseCase(
            ProjectRepository projectRepository,
            EmployeeProvider currentEmployeeProvider,
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
