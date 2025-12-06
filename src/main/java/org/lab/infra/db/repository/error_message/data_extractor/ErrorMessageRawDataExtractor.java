package org.lab.infra.db.repository.error_message.data_extractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ErrorMessageRawDataExtractor {

    public Map<String, Object> extractErrorMessageRawData(
            ResultSet rs
    ) throws
            SQLException
    {
        Map<String, Object> row = new HashMap<>();
        row.put("id", rs.getInt("id"));
        row.put("projectId", rs.getString("projectId"));
        row.put("createdBy", rs.getInt("createdBy"));
        row.put("text", rs.getString("text"));
        row.put("status", rs.getInt("status"));
        return row;
    }
}
