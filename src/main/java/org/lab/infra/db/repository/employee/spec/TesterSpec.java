package org.lab.infra.db.repository.employee.spec;

import java.util.ArrayList;
import java.util.List;

import org.lab.infra.db.spec.SqlSpec;

public class TesterSpec implements SqlSpec {

    private final int testerId;

    public TesterSpec(int testerId) {
        this.testerId = testerId;
    }

    @Override
    public String toSql() {
        return "? IN \"testerIds\"";
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(this.testerId);
        return params;
    }
}
