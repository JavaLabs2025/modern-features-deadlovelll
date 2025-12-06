package org.lab.infra.db.repository.employee.spec;

import org.lab.infra.db.spec.SqlSpec;

import java.util.ArrayList;
import java.util.List;

public class ManagerSpec implements SqlSpec {

    private final int managerId;

    public ManagerSpec(int managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toSql() {
        return "\"managerId\" = ?";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(this.managerId);
        return params;
    }
}
