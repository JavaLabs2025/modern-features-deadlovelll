package org.lab.infra.db.repository.employee.data_extractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EmployeeRawDataExtractor {

    public Map<String, Object> extractEmployeeRawData(
            ResultSet rs
    ) throws
            SQLException
    {
        Map<String, Object> row = new HashMap<>();
        row.put("id", rs.getInt("id"));
        row.put("name", rs.getString("name"));
        row.put("age", rs.getInt("age"));
        row.put("type", rs.getString("type"));
        row.put("createdBy", rs.getInt("createdBy"));
        row.put("createdDate", rs.getTimestamp("createdDate"));
        return row;
    }
}
