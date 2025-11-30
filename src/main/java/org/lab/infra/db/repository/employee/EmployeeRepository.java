package org.lab.infra.db.repository.employee;

import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.client.DatabaseClient;

public class EmployeeRepository {
    private DatabaseClient databaseClient;

    public EmployeeRepository() {
        databaseClient = new DatabaseClient();
    }

    public Employee getById(int id) {
        return new Employee();
    }

    public Employee create(Employee employee) {
        return new Employee();
    }

    public Object delete(int id) {
        return null;
    }

    public Employee update(Employee employee) {
        return null;
    }
}
