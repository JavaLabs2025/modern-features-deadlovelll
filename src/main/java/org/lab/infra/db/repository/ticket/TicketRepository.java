package org.lab.infra.db.repository.ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.shared.exceptions.DatabaseException;
import org.lab.domain.ticket.model.Ticket;
import org.lab.infra.db.client.DatabaseClient;
import org.lab.infra.db.repository.ticket.data_extractor.TicketRawDataExtractor;

public class TicketRepository {

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;
    private final TicketRawDataExtractor ticketRawDataExtractor;

    public TicketRepository() {
        databaseClient = new DatabaseClient();
        objectMapper = new ObjectMapper();
        ticketRawDataExtractor = new TicketRawDataExtractor();
    }

    public Ticket get(
            int ticketId,
            int employeeId
    ) {
        String sql = "SELECT * FROM tickets WHERE id = ? AND \"assignedTo\" = ?";
        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, ticketId);
            stmt.setInt(2, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> row = ticketRawDataExtractor.extractTicketRawData(rs);
                    return objectMapper.mapFromRaw(row, Ticket.class);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public Ticket create(
            Ticket ticket,
            int employeeId,
            int projectId
    ) {
        String sql = """
        INSERT INTO tickets ("createdBy", "assignedTo", description, "projectId")
        VALUES (?, ?, ?, ?)
        RETURNING *
        """;

        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, employeeId);
            stmt.setInt(2, ticket.getAssignedTo());
            stmt.setString(3, ticket.getDescription());
            stmt.setInt(4, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> row = ticketRawDataExtractor.extractTicketRawData(rs);
                    return objectMapper.mapFromRaw(row, Ticket.class);
                } else {
                    throw new RuntimeException("Ticket creation failed: no row returned");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public Ticket close(
            int ticketId
    ) {
        String sql = """
        UPDATE tickets SET status = 'CLOSED' WHERE id = ?";
        """;
        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, ticketId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> row = ticketRawDataExtractor.extractTicketRawData(rs);
                    return objectMapper.mapFromRaw(row, Ticket.class);
                } else {
                    throw new RuntimeException("Ticket update failed: no row returned");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }
}
