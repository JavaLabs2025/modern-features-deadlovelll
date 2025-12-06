package org.lab.infra.db.repository.employee.spec;

import org.lab.infra.db.spec.SqlSpec;

import java.util.ArrayList;
import java.util.List;

public class TeamLeaderSpec implements SqlSpec {

    private final int teamLeaderId;

    public TeamLeaderSpec(int teamLeaderId) {
        this.teamLeaderId = teamLeaderId;
    }

    @Override
    public String toSql() {
        return "\"teamLeadId\" = ?";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(this.teamLeaderId);
        return params;
    }
}
