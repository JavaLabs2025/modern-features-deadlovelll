package org.lab.infra.db.repository.ticket;

import org.lab.domain.emploee.model.Employee;
import org.lab.domain.shared.exceptions.DatabaseException;
import org.lab.domain.ticket.model.Ticket;
import org.lab.infra.db.client.DatabaseClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class TicketRepository {

    public Ticket get(int ticketId) {
        return new Ticket();
    }

    public Ticket create(
            Ticket ticket,
            int employeeId,
            int projectId
    ) {
        String sql = """
        INSERT INTO tickets (createdBy, assignedTo, description, "createdBy")
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
}
