package org.lab.application.project.services;

import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.repository.employee.spec.TeamLeaderSpec;
import org.lab.infra.db.repository.employee.spec.TesterSpec;
import org.lab.infra.db.repository.employee.spec.DeveloperSpec;
import org.lab.infra.db.repository.employee.spec.ManagerSpec;
import org.lab.infra.db.spec.Specification;

public class UserSpecFactory {

    public Specification getForType(
            Employee employee
    ) {
        switch (employee.getType()) {
            case MANAGER:
                return new ManagerSpec(employee.getId());
            case TEAMLEAD:
                return new TeamLeaderSpec(employee.getId());
            case PROGRAMMER:
                return new DeveloperSpec(employee.getId());
            case TESTER:
                return new TesterSpec(employee.getId());
        }
        return null;
    }
}
