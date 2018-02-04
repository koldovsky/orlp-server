package com.softserve.academy.spaced.repetition.service.cardLoaders.impl;

import com.softserve.academy.spaced.repetition.service.cardLoaders.CardDataExtractor;
import com.softserve.academy.spaced.repetition.service.cardLoaders.DbConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AnkiCardExtractor implements CardDataExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnkiCardExtractor.class);
    private static final String QUESTION_QUERY = "SELECT sfld FROM notes";
    private static final String ANSWER_QUERY = "SELECT flds FROM notes";
    private static final String QUESTION_COLUMN_NAME = "sfld";
    private static final String ANSWER_COLUMN_NAME = "flds";
    private static final String REGEX_FOR_TAGS = "<[^>]*>";
    @Autowired
    @Qualifier("cardConnector")
    private DbConnector connector;

    @Override
    public Map<String, String> extractData(String path) throws SQLException, ClassNotFoundException {
        Connection connection = connector.getConnection(path);
        List<String> questions = extractDateInToList(connection, QUESTION_QUERY, QUESTION_COLUMN_NAME);
        List<String> answers = extractDateInToList(connection, ANSWER_QUERY, ANSWER_COLUMN_NAME);
        answers = deleteTags(answers, questions);
        return formMap(questions, answers);
    }

    private List<String> extractDateInToList(Connection conn, String query, String columnName) {
        List<String> list = new ArrayList<>();
        try (Statement statement = conn.createStatement();
             ResultSet res = statement.executeQuery(query)) {
            while (res.next()) {
                list.add(res.getString(columnName));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());

        }
        return list;
    }

    private List<String> deleteTags(List<String> questions, List<String> answers) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(REGEX_FOR_TAGS);
        for (String elem : questions) {
            Matcher matcher = pattern.matcher(elem);
            result.add(matcher.replaceAll(""));
        }
        return result;
    }

    private Map<String, String> formMap(List<String> questions, List<String> answers) {
        Map<String, String> map = new HashMap<>();
        Iterator<String> iterAnswer = answers.iterator();
        Iterator<String> iterQuestion = questions.iterator();
        while (iterAnswer.hasNext() && iterQuestion.hasNext()) {
            String question = iterQuestion.next();
            String clearedAnswer = iterAnswer.next().replace(question, " ");
            map.put(question, clearedAnswer);
        }
        return map;
    }
}
