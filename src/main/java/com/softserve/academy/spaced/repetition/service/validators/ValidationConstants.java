package com.softserve.academy.spaced.repetition.service.validators;

public class ValidationConstants {

    private ValidationConstants() {
    }

    public static final int PERSON_FIELD_MIN_SIZE = 2;

    public static final int PERSON_FIELD_MAX_SIZE = 15;

    public static final String SPECIAL_SYMBOLS_PATTERN = "[^`~!@#$%^&*()\\-_=\\+\\[\\]{};:\\'\\\".>/?,<\\|]*";

    public static final String NULL_MESSAGE = "Field can not be null";

    public static final String EMPTY_MESSAGE = "Field can not be empty";

    public static final String PERSON_FIELD_SIZE_MESSAGE = "Can not have less than " + PERSON_FIELD_MIN_SIZE
            + " and more than " + PERSON_FIELD_MAX_SIZE + " symbols";

    public static final String SPECIAL_SYMBOLS_PATTERN_MESSAGE = "Can not consist of reserved symbols only";

    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
            "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final String EMAIL_PATTERN_MESSAGE = "Incorrect format";

    public static final String EMAIL_EXIST_MESSAGE = "Email already exists";

    public static final int PASSWORD_MIN_SIZE = 8;

    public static final int PASSWORD_MAX_SIZE = 20;

    public static final String PASSWORD_SIZE_MESSAGE = "Can not have less than " + PASSWORD_MIN_SIZE
            + " and more than " + PASSWORD_MAX_SIZE + " symbols";

    public static final String PASSWORD_MATCHES_MESSAGE = "Password should match with current";
}
