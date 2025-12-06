package org.lab.application.error_message.services;

import com.google.inject.Inject;

import org.lab.application.shared.services.EmployeeProvider;
import org.lab.application.shared.services.ProjectSpecProvider;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.NotPermittedException;
import org.lab.infra.db.spec.Specification;
import org.lab.application.project.services.UserSpecFactory;

public class CreateErrorMessageValidator {

    private final EmployeeProvider employeeProvider;
    private final ProjectSpecProvider projectSpecProvider;
    private final UserSpecFactory userSpecFactory;

    @Inject
    public CreateErrorMessageValidator(
            EmployeeProvider employeeProvider,
            ProjectSpecProvider projectSpecProvider,
            UserSpecFactory userSpecFactory
    ) {
        this.employeeProvider = employeeProvider;
        this.projectSpecProvider = projectSpecProvider;
        this.userSpecFactory = userSpecFactory;
    }

    public void validate(
            int employeeId,
            int projectId
    ) {
        Employee employee = this.employeeProvider.get(employeeId);
        if (employee.getType() != EmployeeType.TESTER) {
            throw new NotPermittedException("Only tester can create error messages");
        }
        Specification employeeSpec = this.userSpecFactory.getForType(employee);
        this.projectSpecProvider.get(projectId, employeeSpec);
    }
}
