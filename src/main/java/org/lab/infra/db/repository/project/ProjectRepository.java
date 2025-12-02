package org.lab.infra.db.repository.project;

import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.project.model.Project;
import org.lab.infra.db.client.DatabaseClient;

public class ProjectRepository {

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;

    public ProjectRepository() {
        databaseClient = new DatabaseClient();
        objectMapper = new ObjectMapper();
    }

    public Project create(Project project) {
        return new Project();
    }
}
