package org.lab.infra.db.repository.employee;

import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.DatabaseException;
import org.lab.infra.db.client.DatabaseClient;
import org.lab.core.utils.mapper.ObjectMapper;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class EmployeeRepository {
    
    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;

    public EmployeeRepository() {
        databaseClient = new DatabaseClient();
        objectMapper = new ObjectMapper();
    }

    public Employee getById(int id) {
        String sql = "SELECT * FROM employees where id = ?";
        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    Map<String, Object> raw = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnLabel(i);
                        Object value = rs.getObject(i);
                        raw.put(columnName, value);
                    }
                    System.out.println("value = " + raw);
                    return objectMapper.mapFromRaw(raw, Employee.class);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
        return null;
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
}
