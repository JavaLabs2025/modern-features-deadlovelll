package org.lab.infra.db.repository.project;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Array;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.project.model.Project;
import org.lab.domain.shared.exceptions.DatabaseException;
import org.lab.infra.db.client.DatabaseClient;
import org.lab.infra.db.spec.Specification;
import org.lab.infra.db.spec.SqlSpec;
import org.lab.infra.db.repository.project.data_extractor.ProjectRawDataExtractor;

public class ProjectRepository {

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;
    private final ProjectRawDataExtractor projectRawDataExtractor;

    public ProjectRepository() {
        databaseClient = new DatabaseClient();
        objectMapper = new ObjectMapper();
        projectRawDataExtractor = new ProjectRawDataExtractor();
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
                    Map<String, Object> row = projectRawDataExtractor.extractRawData(rs);
                    return objectMapper.mapFromRaw(row, Project.class);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public Project getWithSpec(
            int projectId,
            Specification spec
    ) {
        SqlSpec sqlSpec = (SqlSpec) spec;
        String sql = "SELECT * FROM projects WHERE id = ? AND " + sqlSpec.toSql();
        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, projectId);
            List<Object> params = sqlSpec.getParams();
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i+2, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> row = projectRawDataExtractor.extractRawData(rs);
                    return objectMapper.mapFromRaw(row, Project.class);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public Project create(
            Project project,
            int employeeId
    ) {
        String sql = """
            INSERT INTO projects (
                name,
                description,
                "managerId",
                "teamLeadId",
                "developerIds",
                "testerIds",
                "createdBy"
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING *
        """;

        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());
            stmt.setInt(3, employeeId);
            stmt.setInt(4, project.getTeamLeadId());
            Array developerArray = conn.createArrayOf(
                    "INTEGER",
                    project.getDeveloperIds().toArray()
            );
            stmt.setArray(5, developerArray);

            Array testerArray = conn.createArrayOf(
                    "INTEGER",
                    project.getTesterIds().toArray()
            );
            stmt.setArray(6, testerArray);
            stmt.setInt(7, project.getCreatedBy());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> row = projectRawDataExtractor.extractRawData(rs);
                    return objectMapper.mapFromRaw(row, Project.class);
                } else {
                    throw new RuntimeException("Project creation failed: no row returned");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public List<Project> list(
            Specification specification
    ) {
        SqlSpec spec = (SqlSpec) specification;
        String sql = "SELECT * FROM projects WHERE " + spec.toSql();
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
                    Map<String, Object> row = projectRawDataExtractor.extractRawData(rs);
                    projects.add(objectMapper.mapFromRaw(row, Project.class));
                }
                return projects;
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public int delete(int projectId) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (
                Connection conn = DatabaseClient.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, projectId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }
}
