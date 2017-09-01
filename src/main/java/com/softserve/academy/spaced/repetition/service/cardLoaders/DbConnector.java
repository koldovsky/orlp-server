package com.softserve.academy.spaced.repetition.service.cardLoaders;

import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service
public abstract class DbConnector {
    private DbConnector connector;

    public abstract Connection getConnection(String path);

    public void setConnector(DbConnector connector) {
        this.connector = connector;
    }
}
