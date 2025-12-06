package org.lab.infra.db.repository.project.data_extractor;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lab.core.constants.project.ProjectStatus;

public class ProjectRawDataExtractor {

    public Map<String, Object> extractRawData(
            ResultSet rs
    ) throws
            SQLException
    {
        Map<String, Object> row = new HashMap<>();
        row.put("id", rs.getInt("id"));
        row.put("name", rs.getString("name"));
        row.put("description", rs.getString("description"));
        row.put("managerId", rs.getInt("managerId"));

        int teamLeadId = rs.getInt("teamLeadId");
        row.put("teamLeadId", rs.wasNull() ? null : teamLeadId);

        Array developerArrayRes = rs.getArray("developerIds");
        List<Integer> developerIds = developerArrayRes != null
                ? Arrays.asList((Integer[]) developerArrayRes.getArray())
                : List.of();
        row.put("developerIds", developerIds);

        Array testerArrayRes = rs.getArray("testerIds");
        List<Integer> testerIds = testerArrayRes != null
                ? Arrays.asList((Integer[]) testerArrayRes.getArray())
                : List.of();
        row.put("testerIds", testerIds);

        row.put("status", ProjectStatus.valueOf(rs.getString("status")));

        int currentMilestoneId = rs.getInt("currentMilestoneId");
        row.put("currentMilestoneId", rs.wasNull() ? null : currentMilestoneId);

        Array milestoneArray = rs.getArray("milestoneIds");
        List<Integer> milestoneIds = milestoneArray != null
                ? Arrays.asList((Integer[]) milestoneArray.getArray())
                : List.of();
        row.put("milestoneIds", milestoneIds);

        Array bugArray = rs.getArray("bugReportIds");
        List<Integer> bugReportIds = bugArray != null
                ? Arrays.asList((Integer[]) bugArray.getArray())
                : List.of();
        row.put("bugReportIds", bugReportIds);

        row.put("createdBy", rs.getInt("createdBy"));
        row.put("createdDate", rs.getTimestamp("createdDate"));
        row.put(
                "updatedBy",
                rs.getObject("updatedBy") != null
                        ? rs.getInt("updatedBy") : null
        );
        row.put("updatedDate", rs.getTimestamp("updatedDate"));
        return row;
    }
}
