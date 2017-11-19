package com.softserve.academy.spaced.repetition.service.validators;

public class ValidationConstants {

    public final static int PERSON_FIELD_MIN_LENGTH = 2;

    public final static int PERSON_FIELD_MAX_LENGTH = 15;

    public final static String SPECIAL_SYMBOLS_PATTERN = "[^`~!@#$%^&*()\\-_=\\+\\[\\]{};:\\'\\\".>/?,<\\|]*";

    public final static String NULL_MESSAGE = "Field can not be null";

    public final static String EMPTY_MESSAGE = "Field can not be empty";

    public final static String PERSON_FIELD_LENGTH_MESSAGE = "Can not have less than " + PERSON_FIELD_MIN_LENGTH
            + " and more than " + PERSON_FIELD_MAX_LENGTH + " symbols";

    public final static String SPECIAL_SYMBOLS_PATTERN_MESSAGE = "Should consist of letters only";

    public final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
            "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public final static String EMAIL_PATTERN_MESSAGE = "Incorrect format";

    public final static int PASSWORD_MIN_LENGTH = 8;

    public final static int PASSWORD_MAX_LENGTH = 20;

    public final static String PASSWORD_LENGTH_MESSAGE = "Can not have less than " + PASSWORD_MIN_LENGTH
            + " and more than " + PASSWORD_MAX_LENGTH + " symbols";
}
