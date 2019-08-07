package com.solvd.webappsimple.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static final String SCHEMA_NAME = "solvd_events";
    private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "root";

    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(DRIVER_CLASS_NAME);
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.prepareStatement(String.format("SET search_path TO '%s';", SCHEMA_NAME)).execute();
        return connection;
    }

}
