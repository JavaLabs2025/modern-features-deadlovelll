package org.lab.application.project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.lab.core.constants.employee.EmployeeType;
import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.repository.employee.spec.DeveloperSpec;
import org.lab.infra.db.repository.employee.spec.ManagerSpec;
import org.lab.infra.db.repository.employee.spec.TeamLeaderSpec;
import org.lab.infra.db.repository.employee.spec.TesterSpec;
import org.lab.infra.db.spec.Specification;

class TestUserSpecFactory {

    private UserSpecFactory factory;

    @BeforeEach
    void setup() {
        factory = new UserSpecFactory();
    }

    @Test
    void getForType_manager_returnsManagerSpec() {
        Employee emp = new Employee();
        emp.setId(1);
        emp.setType(EmployeeType.MANAGER);

        Specification spec = factory.getForType(emp);

        assertTrue(spec instanceof ManagerSpec);
        assertEquals(1, ((ManagerSpec) spec).getParams().get(0));
    }

    @Test
    void getForType_teamLead_returnsTeamLeaderSpec() {
        Employee emp = new Employee();
        emp.setId(2);
        emp.setType(EmployeeType.TEAMLEAD);

        Specification spec = factory.getForType(emp);

        assertTrue(spec instanceof TeamLeaderSpec);
        assertEquals(2, ((TeamLeaderSpec) spec).getParams().get(0));
    }

    @Test
    void getForType_programmer_returnsDeveloperSpec() {
        Employee emp = new Employee();
        emp.setId(3);
        emp.setType(EmployeeType.PROGRAMMER);

        Specification spec = factory.getForType(emp);

        assertTrue(spec instanceof DeveloperSpec);
        assertEquals(3, ((DeveloperSpec) spec).getParams().get(0));
    }

    @Test
    void getForType_tester_returnsTesterSpec() {
        Employee emp = new Employee();
        emp.setId(4);
        emp.setType(EmployeeType.TESTER);

        Specification spec = factory.getForType(emp);

        assertTrue(spec instanceof TesterSpec);
        assertEquals(4, ((TesterSpec) spec).getParams().get(0));
    }
}
