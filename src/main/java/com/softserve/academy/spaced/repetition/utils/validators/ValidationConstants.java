package com.softserve.academy.spaced.repetition.utils.validators;

public class ValidationConstants {

    private ValidationConstants() {
    }

    public static final int PERSON_FIELD_MIN_SIZE = 2;

    public static final int PERSON_FIELD_MAX_SIZE = 15;

    public static final String SPECIAL_SYMBOLS_PATTERN = "[^`~!@#$%^&*()\\-_=\\+\\[\\]{};:\\'\\\".>/?,<\\|]*";

    public static final String NULL_MESSAGE = "Field can not be null";

    public static final String EMPTY_MESSAGE = "Field can not be empty";

    private static final String FIRST_PART_OF_SIZE_MESSAGE = "Can not have less than ";

    private static final String SECOND_PART_OF_SIZE_MESSAGE = " and more than ";

    private static final String THIRD_PART_OF_SIZE_MESSAGE = " symbols";

    public static final String PERSON_FIELD_SIZE_MESSAGE = FIRST_PART_OF_SIZE_MESSAGE + PERSON_FIELD_MIN_SIZE
            + SECOND_PART_OF_SIZE_MESSAGE + PERSON_FIELD_MAX_SIZE + THIRD_PART_OF_SIZE_MESSAGE;

    public static final String SPECIAL_SYMBOLS_PATTERN_MESSAGE = "Can not consist of reserved symbols";

    public static final int EMAIL_MIN_SIZE = 3;

    public static final int EMAIL_MAX_SIZE = 254;

    public static final String EMAIL_SIZE_MESSAGE = FIRST_PART_OF_SIZE_MESSAGE + EMAIL_MIN_SIZE
            + SECOND_PART_OF_SIZE_MESSAGE + EMAIL_MAX_SIZE + THIRD_PART_OF_SIZE_MESSAGE;

    public static final String EMAIL_PATTERN =
            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"+" +
                    "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                    "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?" +
                    "[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-" +
                    "\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static final String EMAIL_PATTERN_MESSAGE = "Incorrect format";

    public static final String EMAIL_NOT_EXIST_MESSAGE = "Email already exists";

    public static final int PASS_MIN_SIZE = 8;

    public static final int PASS_MAX_SIZE = 20;

    public static final String PASS_SIZE_MESSAGE = FIRST_PART_OF_SIZE_MESSAGE + PASS_MIN_SIZE
            + SECOND_PART_OF_SIZE_MESSAGE + PASS_MAX_SIZE + THIRD_PART_OF_SIZE_MESSAGE;

    public static final String PASS_MATCHES_MESSAGE = "Password should match with current";

    public static final int MIN_COURSE_AND_CARD_RATING = 1;

    public static final int MAX_COURSE_AND_CARD_RATING = 5;

    public static final String COURSE_AND_CARD_RATING_ERROR_MESSAGE = "Rating can't be less than 1 and more than 5";
}
