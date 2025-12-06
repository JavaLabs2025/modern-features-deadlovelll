package org.lab.infra.db.spec;

import java.util.List;

public sealed interface Specification
        permits SqlSpec
{
    String toSql();
    List<Object> getParams();
}
