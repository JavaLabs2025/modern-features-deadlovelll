package org.lab.infra.db.spec;

import java.util.List;

public non-sealed interface SqlSpec
        extends Specification {
    String toSql();
    List<Object> getParams();
}
