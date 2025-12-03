package org.lab.infra.db.repository.project;

import java.util.ArrayList;
import java.util.List;

import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.project.model.Project;
import org.lab.infra.db.client.DatabaseClient;
import org.lab.infra.db.spec.Specification;

public class ProjectRepository {

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;

    public ProjectRepository() {
        databaseClient = new DatabaseClient();
        objectMapper = new ObjectMapper();
    }

    public Project get(int projectId) {
        return new Project();
    }

    public Project create(Project project) {
        return new Project();
    }

    public List<Project> list(
            Specification specification
    ) {
        return new ArrayList<>();
    }

    public void delete(int projectId) {}
}
