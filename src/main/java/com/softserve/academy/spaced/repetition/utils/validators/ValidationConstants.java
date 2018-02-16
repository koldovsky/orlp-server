package com.softserve.academy.spaced.repetition.utils.validators;

public class ValidationConstants {
    public static final int MAX_SHOW_DECK_SIZE = 10;
    public static final int MAX_CARD_TITLE_SIZE = 50;
    public static final int MAX_CARD_IMAGE_LIST_SIZE = 5;
    public static final int PERSON_FIELD_MIN_SIZE = 2;
    public static final int PERSON_FIELD_MAX_SIZE = 15;
    public static final int MIN_COURSE_AND_CARD_RATING = 1;
    public static final int MIN_NUMBER_OF_CARDS = 1;
    public static final int MAX_COURSE_AND_CARD_RATING = 5;
    public static final int PASSWORD_MIN_SIZE = 8;
    public static final int PASSWORD_MAX_SIZE = 20;
    public static final int PASSWORD_MAX_SIZE_HASH = 60;
    public static final int EMAIL_MIN_SIZE = 3;
    public static final int EMAIL_MAX_SIZE = 254;
    public static final int AUTH_TYPE_MAX_SIZE = 15;
    public static final int AUTH_NAME_MAX_SIZE = 50;
    public static final int IMAGE_TYPE_MAX_SIZE = 6;
    public static final String EMAIL_PATTERN =
            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"+" +
                    "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                    "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?" +
                    "[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-" +
                    "\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final String SPECIAL_SYMBOLS_PATTERN = "[^`~!@#$%^&*()\\-_=\\+\\[\\]{};:\\'\\\".>/?,<\\|]*";

    private ValidationConstants() {
    }
}
