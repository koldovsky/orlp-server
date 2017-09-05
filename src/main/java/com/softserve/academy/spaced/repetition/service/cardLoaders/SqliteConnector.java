package com.softserve.academy.spaced.repetition.service.cardLoaders;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class SqliteConnector extends DbConnector {
    private final static String DRIVER = "org.sqlite.JDBC";
    private final static String PATH = "jdbc:sqlite:";

    @Override
    public Connection getConnection(String relativePath) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName(DRIVER);
        conn = DriverManager.getConnection(PATH + relativePath);
        System.out.println("Connection to SQLite has been established.");
        return conn;
    }
}
