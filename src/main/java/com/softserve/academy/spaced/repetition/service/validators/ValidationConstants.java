package com.softserve.academy.spaced.repetition.service.validators;

public class ValidationConstants {

    public final static int PERSON_FIELD_MIN_SIZE = 2;

    public final static int PERSON_FIELD_MAX_SIZE = 15;

    public final static String SPECIAL_SYMBOLS_PATTERN = "[^`~!@#$%^&*()\\-_=\\+\\[\\]{};:\\'\\\".>/?,<\\|]*";

    public final static String NULL_MESSAGE = "Field can not be null";

    public final static String EMPTY_MESSAGE = "Field can not be empty";

    public final static String PERSON_FIELD_SIZE_MESSAGE = "Can not have less than " + PERSON_FIELD_MIN_SIZE
            + " and more than " + PERSON_FIELD_MAX_SIZE + " symbols";

    public final static String SPECIAL_SYMBOLS_PATTERN_MESSAGE = "Can not consist of reserved symbols only";

    public final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
            "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public final static String EMAIL_PATTERN_MESSAGE = "Incorrect format";

    public final static int PASSWORD_MIN_SIZE = 8;

    public final static int PASSWORD_MAX_SIZE = 20;

    public final static String PASSWORD_SIZE_MESSAGE = "Can not have less than " + PASSWORD_MIN_SIZE
            + " and more than " + PASSWORD_MAX_SIZE + " symbols";
}
