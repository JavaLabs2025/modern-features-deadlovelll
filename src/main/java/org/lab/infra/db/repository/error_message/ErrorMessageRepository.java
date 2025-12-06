package org.lab.infra.db.repository.error_message;

import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.error_mesage.model.ErrorMessage;
import org.lab.domain.shared.exceptions.DatabaseException;
import org.lab.infra.db.client.DatabaseClient;
import org.lab.infra.db.repository.error_message.data_extractor.ErrorMessageRawDataExtractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ErrorMessageRepository {

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;
    private final ErrorMessageRawDataExtractor errorMessageRawDataExtractor;

    public ErrorMessageRepository() {
        databaseClient = new DatabaseClient();
        objectMapper = new ObjectMapper();
        errorMessageRawDataExtractor = new ErrorMessageRawDataExtractor();
    }

    public ErrorMessage create(
            ErrorMessage message,
            int employeeId
    ) {
        String sql = """
        INSERT INTO error_messages (projectId, createdBy, text)
        VALUES (?, ?, ?,)
        RETURNING *
        """;

        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, message.getProjectId());
            stmt.setInt(2, employeeId);
            stmt.setString(3, message.getText());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> row = errorMessageRawDataExtractor.extractErrorMessageRawData(rs);
                    return objectMapper.mapFromRaw(row, ErrorMessage.class);
                } else {
                    throw new RuntimeException("Employee creation failed: no row returned");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }
}
