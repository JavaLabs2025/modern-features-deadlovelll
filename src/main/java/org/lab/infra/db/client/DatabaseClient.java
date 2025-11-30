package org.lab.infra.db.client;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseClient {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://postgres:5432/mydb");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setMaximumPoolSize(10); // размер пула
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection()
            throws SQLException
    {
        return dataSource.getConnection();
    }
}
