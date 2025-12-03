package org.lab.infra.db.repository.employee.spec;

import org.lab.infra.db.spec.SqlSpec;

import java.util.ArrayList;
import java.util.List;

public class DeveloperSpec implements SqlSpec {

    private final int developerId;

    public DeveloperSpec(int developerId) {
        this.developerId = developerId;
    }

    @Override
    public String toSql() {
        return "? IN developerIds";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(this.developerId);
        return params;
    }
}
