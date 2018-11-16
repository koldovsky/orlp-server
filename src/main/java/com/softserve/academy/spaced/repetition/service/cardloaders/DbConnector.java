package com.softserve.academy.spaced.repetition.service.cardloaders;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public interface DbConnector {
    Connection getConnection(String path) throws ClassNotFoundException, SQLException;

}
