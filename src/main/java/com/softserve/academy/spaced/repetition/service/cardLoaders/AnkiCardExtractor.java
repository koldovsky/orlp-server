package com.softserve.academy.spaced.repetition.service.cardLoaders;

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
    private final static String QUESTION_QUERY = "SELECT sfld FROM notes";
    private final static String ANSWER_QUERY = "SELECT flds FROM notes";
    private final static String QUESTION_COLUMN_NAME = "sfld";
    private final static String ANSWER_COLUMN_NAME = "flds";
    private final static String REGEX_FOR_TAGS = "<[^>]*>";
    @Autowired
    @Qualifier("cardConnector")
    private DbConnector connector;

    @Override
    public Map <String, String> extractData(String path) throws SQLException, ClassNotFoundException {
        Connection connection = connector.getConnection(path);
        List <String> questions = _extractDate(connection, QUESTION_QUERY, QUESTION_COLUMN_NAME);
        List <String> answers = _extractDate(connection, ANSWER_QUERY, ANSWER_COLUMN_NAME);
        answers = deleteTags(answers, questions);
        return formMap(questions, answers);
    }

    private List <String> _extractDate(Connection conn, String query, String columnName) throws SQLException {
        List <String> list = new ArrayList <String>();
        Statement statement = null;
        ResultSet res = null;
        statement = conn.createStatement();
        res = statement.executeQuery(query);
        while (res.next()) {
            list.add(res.getString(columnName));
        }
        return list;
    }

    private List <String> deleteTags(List <String> questions, List <String> answers) {
        List <String> result = new ArrayList <String>();
        Pattern pattern = Pattern.compile(REGEX_FOR_TAGS);
        Iterator <String> iter = answers.iterator();
        for (String elem : questions) {
            Matcher matcher = pattern.matcher(elem);
            result.add(matcher.replaceAll(""));
        }
        return result;
    }

    private Map <String, String> formMap(List <String> questions, List <String> answers) {
        Map <String, String> map = new HashMap <String, String>();
        Iterator <String> iterAnswer = answers.iterator();
        Iterator <String> iterQuestion = questions.iterator();
        while (iterAnswer.hasNext() && iterQuestion.hasNext()) {
            String question = iterQuestion.next();
            String clearedAnswer = iterAnswer.next().replace(question, " ");
            map.put(question, clearedAnswer);
        }
        return map;
    }
}
