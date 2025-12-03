package org.lab.infra.db.repository.employee.spec;

import org.lab.infra.db.spec.SqlSpec;

import java.util.ArrayList;
import java.util.List;

public class TesterSpec implements SqlSpec {

    private final int testerId;

    public TesterSpec(int testerId) {
        this.testerId = testerId;
    }

    @Override
    public String toSql() {
        return "? IN testerIds";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(this.testerId);
        return params;
    }
}
