package org.lab.infra.db.repository.project;

import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.infra.db.client.DatabaseClient;

public class ProjectRepository {

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;

    public ProjectRepository() {
        databaseClient = new DatabaseClient();
        objectMapper = new ObjectMapper();
    }
}
