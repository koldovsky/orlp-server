package com.softserve.academy.spaced.repetition.service.cardLoaders;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class SqliteConnector extends DbConnector {
    private final static String DRIVER = "org.sqlite.JDBC";
    private String path = "jdbc:sqlite:";


    @Override
    public Connection getConnection(String relativePath) {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(path + relativePath);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
