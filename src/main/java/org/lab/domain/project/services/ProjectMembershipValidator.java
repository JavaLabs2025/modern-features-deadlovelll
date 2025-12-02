package org.lab.domain.project.services;

import org.lab.domain.emploee.model.Employee;
import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.NotPermittedException;

public class ProjectMembershipValidator {

    public void validate(
            Employee employee,
            Project project
    ) throws
            NotPermittedException
    {
        switch (employee.getType()) {
            case PROGRAMMER -> {
                if (!project.getDeveloperIds().contains(employee.getId())) {
                    throw new NotPermittedException("You are not permitted to perform this operation");
                }
            }
            case TESTER -> {
                if (!project.getTesterIds().contains(employee.getId())) {
                    throw new NotPermittedException("You are not permitted to perform this operation");
                }
            }
            case MANAGER -> {
                if (project.getManagerId() != employee.getId()) {
                    throw new NotPermittedException("You are not permitted to perform this operation");
                }
            }
            case TEAMLEAD ->  {
                if (project.getTeamLeadId() != employee.getId()) {
                    throw new NotPermittedException("You are not permitted to perform this operation");
                }
            }
        }
    }
}
