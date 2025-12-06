package org.lab.application.shared.services;

import org.lab.infra.db.spec.SqlSpec;

import java.util.List;

public final class DummySpec implements SqlSpec {
    @Override
    public String toSql() {
        return "1=1";
    }

    @Override
    public List<Object> getParams() {
        return List.of();
    }
}

