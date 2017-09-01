package com.softserve.academy.spaced.repetition.service.cardLoaders;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final static String QUESTIONQUERY = "SELECT sfld FROM notes";
    private final static String ANSWERQUERY = "SELECT flds FROM notes";
    private final static String QUESTIONCOLUMNNAME = "sfld";
    private final static String ANSWERCOLUMNNAME = "flds";
    private final static String REGEXFORTAGS = "<[^>]*>";
    @Autowired
    private SqliteConnector connector;

    @Override
    public Map <String, String> extractData(String path) {
        connector.setConnector(connector);
        Connection connection = connector.getConnection(path);
        List <String> questions = _extractDate(connection, QUESTIONQUERY, QUESTIONCOLUMNNAME);
        List <String> answers = _extractDate(connection, ANSWERQUERY, ANSWERCOLUMNNAME);
        answers = deleteTags(answers, questions);
        return formMap(questions, answers);
    }

    private List <String> _extractDate(Connection conn, String query, String columnName) {
        List <String> list = new ArrayList <String>();
        Statement statement = null;
        ResultSet res = null;
        try {
            statement = conn.createStatement();
            res = statement.executeQuery(query);
            while (res.next()) {
                list.add(res.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List <String> deleteTags(List <String> questions, List <String> answers) {
        List <String> result = new ArrayList <String>();
        Pattern pattern = Pattern.compile(REGEXFORTAGS);
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
