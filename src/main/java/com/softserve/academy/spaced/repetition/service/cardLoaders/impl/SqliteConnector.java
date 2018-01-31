package com.softserve.academy.spaced.repetition.service.cardLoaders.impl;

import com.softserve.academy.spaced.repetition.service.cardLoaders.DbConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

@Service
public class SqliteConnector implements DbConnector {
    /**
     * Driver to sqlite DataBase. Depends on our uploaded file.
     */
    private final static String DRIVER = "org.sqlite.JDBC";
    /**
     * First part of the url for getting Connection. Secong part is relative path to file
     */
    private final static String PATH = "jdbc:sqlite:";

    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    /**
     * Upload anki cards
     *
     * @return - Connection
     * @throws ClassNotFoundException - is dropping when classloader failed in loading Driver to uploading file.
     * @throws SQLException           - is dropping when file is not found.
     * @see AnkiCardUploader
     */
    @Override
    public Connection getConnection(String relativePath) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName(DRIVER);
        conn = DriverManager.getConnection(PATH + relativePath);
        System.out.println(messageSource.getMessage("message.database.connectionSuccessful",
                new Object[]{"SQLite"}, locale));
        return conn;
    }
}
