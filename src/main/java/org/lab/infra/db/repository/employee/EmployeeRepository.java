package org.lab.infra.db.repository.employee;

import java.sql.*;
import java.util.Map;

import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.DatabaseException;
import org.lab.infra.db.client.DatabaseClient;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.infra.db.repository.employee.data_extractor.EmployeeRawDataExtractor;

public class EmployeeRepository {
    
    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;
    private final EmployeeRawDataExtractor employeeRawDataExtractor;

    public EmployeeRepository() {
        databaseClient = new DatabaseClient();
        objectMapper = new ObjectMapper();
        employeeRawDataExtractor = new EmployeeRawDataExtractor();
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
                    Map<String, Object> row = employeeRawDataExtractor.extractEmployeeRawData(rs);
                    return objectMapper.mapFromRaw(row, Employee.class);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public Employee create(
            Employee employee,
            int actorId
    ) {
        String sql = """
        INSERT INTO employees (name, age, type, "createdBy")
        VALUES (?, ?, ?, ?)
        RETURNING *
        """;

        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, employee.getName());
            stmt.setInt(2, employee.getAge());
            stmt.setString(3, employee.getType().name());
            stmt.setInt(4, actorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> row = employeeRawDataExtractor.extractEmployeeRawData(rs);
                    return objectMapper.mapFromRaw(row, Employee.class);
                } else {
                    throw new RuntimeException("Employee creation failed: no row returned");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public Object delete(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
