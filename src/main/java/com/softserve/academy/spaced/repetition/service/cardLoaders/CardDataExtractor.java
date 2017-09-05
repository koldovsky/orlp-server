package com.softserve.academy.spaced.repetition.service.cardLoaders;

import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;

@Service
public interface CardDataExtractor {
    Map <String, String> extractData(String path) throws SQLException, ClassNotFoundException;

}
