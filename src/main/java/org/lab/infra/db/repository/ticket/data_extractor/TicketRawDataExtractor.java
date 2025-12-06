package org.lab.infra.db.repository.ticket.data_extractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TicketRawDataExtractor {

    public Map<String, Object> extractTicketRawData(
            ResultSet rs
    ) throws
            SQLException
    {
        Map<String, Object> row = new HashMap<>();
        row.put("id", rs.getInt("id"));
        row.put("createdBy", rs.getInt("createdBy"));
        row.put("assignedTo", rs.getInt("assignedTo"));
        row.put("projectId", rs.getInt("projectId"));
        row.put("description", rs.getString("description"));
        row.put("status", rs.getString("status"));
        row.put("createdDate", rs.getTimestamp("createdDate"));
        row.put("closedDate", rs.getTimestamp("closedDate"));
        return row;
    }
}
