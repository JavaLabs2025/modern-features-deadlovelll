package org.lab.infra.db.repository.employee;

import org.lab.domain.emploee.model.Employee;
import org.lab.infra.db.client.DatabaseClient;

import java.sql.*;

public class EmployeeRepository {
    private DatabaseClient databaseClient;

    public EmployeeRepository() {
        databaseClient = new DatabaseClient();
    }

    public Employee getById(int id) {
        return new Employee();
    }

    public Employee create(Employee employee) {
        String sql = """
        INSERT INTO employees (name, age, type, created_by, create_date)
        VALUES (?, ?, ?, ?, ?)
        RETURNING *
        """;

        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, employee.getName());
            stmt.setInt(2, employee.getAge());
            stmt.setString(3, employee.getType().name());
            stmt.setInt(4, employee.getCreatedBy());
            stmt.setTimestamp(5, new Timestamp(
                    employee.getCreatedDate().getTime())
            );
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("mapping should be here");
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return employee;
    }

    public Object delete(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";

        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Employee update(Employee employee) {
        return null;
    }
}
