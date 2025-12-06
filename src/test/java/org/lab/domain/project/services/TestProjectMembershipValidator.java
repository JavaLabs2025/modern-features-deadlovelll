package org.lab.domain.project.services;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.lab.domain.emploee.model.Employee;
import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.NotPermittedException;

public class TestProjectMembershipValidator {

    private ProjectMembershipValidator validator;
    private Employee employee;
    private Project project;

    @BeforeEach
    void setup() {
        validator = new ProjectMembershipValidator();

        project = new Project();
        project.setManagerId(1);
        project.setTeamLeadId(2);
        project.setDeveloperIds(List.of(3, 4, 5));
        project.setTesterIds(List.of(6, 7));
    }

    @Test
    void programmer_allowed() {
        employee = new Employee();
        employee.setId(3);
        employee.setType(EmployeeType.PROGRAMMER);

        assertDoesNotThrow(() -> validator.validate(employee, project));
    }

    @Test
    void programmer_denied() {
        employee = new Employee();
        employee.setId(99);
        employee.setType(EmployeeType.PROGRAMMER);

        assertThrows(NotPermittedException.class, () -> validator.validate(employee, project));
    }

    @Test
    void tester_allowed() {
        employee = new Employee();
        employee.setId(6);
        employee.setType(EmployeeType.TESTER);

        assertDoesNotThrow(() -> validator.validate(employee, project));
    }

    @Test
    void tester_denied() {
        employee = new Employee();
        employee.setId(123);
        employee.setType(EmployeeType.TESTER);

        assertThrows(NotPermittedException.class, () -> validator.validate(employee, project));
    }

    @Test
    void manager_allowed() {
        employee = new Employee();
        employee.setId(1);
        employee.setType(EmployeeType.MANAGER);

        assertDoesNotThrow(() -> validator.validate(employee, project));
    }

    @Test
    void manager_denied() {
        employee = new Employee();
        employee.setId(55);
        employee.setType(EmployeeType.MANAGER);

        assertThrows(NotPermittedException.class, () -> validator.validate(employee, project));
    }

    @Test
    void teamlead_allowed() {
        employee = new Employee();
        employee.setId(2);
        employee.setType(EmployeeType.TEAMLEAD);

        assertDoesNotThrow(() -> validator.validate(employee, project));
    }

    @Test
    void teamlead_denied() {
        employee = new Employee();
        employee.setId(666);
        employee.setType(EmployeeType.TEAMLEAD);

        assertThrows(NotPermittedException.class, () -> validator.validate(employee, project));
    }
}
