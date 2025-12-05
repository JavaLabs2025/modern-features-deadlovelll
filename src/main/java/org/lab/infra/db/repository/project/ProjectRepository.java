package org.lab.infra.db.repository.project;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.project.model.Project;
import org.lab.infra.db.client.DatabaseClient;
import org.lab.infra.db.spec.Specification;
import org.lab.infra.db.spec.SqlSpec;

public class ProjectRepository {

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;

    public ProjectRepository() {
        databaseClient = new DatabaseClient();
        objectMapper = new ObjectMapper();
    }

    public Project get(
            int projectId
    ) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return objectMapper.mapFromRaw(rs, Project.class);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Project create(Project project) {
        String sql = """
            INSERT INTO projects (
                name,
                description,
                managerId,
                teamLeadId,
                developerIds,
                testerIds,
                createdBy
            )
            VALUES (?, ?, ?, ?, ?::jsonb, ?::jsonb, ?)
            RETURNING *
        """;

        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());
            stmt.setInt(3, project.getManagerId());

            if (project.getTeamLeadId() != null)
                stmt.setInt(4, project.getTeamLeadId());
            else
                stmt.setNull(4, Types.INTEGER);

            stmt.setString(5, toJson(project.getDeveloperIds()));
            stmt.setString(6, toJson(project.getTesterIds()));

            stmt.setInt(7, project.getCreatedBy());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return objectMapper.mapFromRaw(rs, Project.class);
                }
            }
        } catch (SQLException | JsonProcessingException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<Project> list(
            Specification specification
    ) {
        SqlSpec spec = (SqlSpec) specification;
        String sql = "SELECT * FROM projects WHERE" + spec.toSql();
        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            List<Object> params = spec.getParams();
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i+1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                List<Project> projects = new ArrayList<>();
                while (rs.next()) {
                    projects.add(objectMapper.mapFromRaw(rs, Project.class));
                }
                return projects;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int projectId) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, projectId);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private String toJson(List<Integer> list)
            throws
            JsonProcessingException
    {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(list);
    }
}
