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
                    return objectMapper.mapFromRaw(raw, Employee.class);
                } else {
                    throw new RuntimeException("Employee creation failed: no row returned");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }}

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
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", rs.getInt("id"));
                    row.put("name", rs.getString("name"));
                    row.put("age", rs.getInt("age"));
                    row.put("type", rs.getString("type"));
                    row.put("createdBy", rs.getInt("createdBy"));
                    row.put("createdDate", rs.getTimestamp("createdDate"));
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
