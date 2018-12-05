package com.softserve.academy.spaced.repetition.utils.audit;

public enum AuditingAction {
    SIGN_IN,
    SIGN_IN_VIA_GOOGLE,
    SIGN_IN_VIA_FACEBOOK,

    SIGN_UP,
    SIGN_UP_GOOGLE,
    SIGN_UP_FACEBOOK,

    VIEW_ALL_USERS_ADMIN,
    VIEW_ONE_USER_ADMIN,
    SET_ACCOUNT_BLOCKED,
    SET_ACCOUNT_DELETED,
    SET_ACCOUNT_ACTIVE,
    ADD_DECK_TO_USER_FOLDER_ADMIN,
    REMOVE_DECK_FROM_USER_FOLDER_ADMIN,
    VIEW_FOLDER_DECKS_ADMIN,
    VIEW_ALL_IMAGE_ADMIN,

    UPLOAD_IMAGE,
    DELETE_IMAGE,

    ADD_DECK_TO_FOLDER,
    VIEW_DECK_IN_FOLDER,
    START_LEARNING_VIA_FOLDER,

    VIEW_ALL_CATEGORIES,
    VIEW_CATEGORY,
    VIEW_TOP_CATEGORIES,
    CREATE_CATEGORY,
    EDIT_CATEGORY,
    DELETE_CATEGORY,

    CREATE_CARD_VIA_COURSE_AND_DECK,
    CREATE_CARD_VIA_CATEGORY_AND_DECK,
    EDIT_CARD_VIA_COURSE_AND_DECK,
    EDIT_CARD_VIA_CATEGORY_AND_DECK,
    DELETE_CARD,
    RATE_CARD,

    VIEW_COURSES,
    VIEW_COURSES_ADMIN,
    VIEW_TOP_COURSES,
    VIEW_COURSES_VIA_CATEGORY,
    ADD_COURSE,
    CREATE_COURSE,
    CREATE_PRIVATE_COURSE,
    EDIT_COURSE_ADMIN,
    DELETE_COURSE,
    DELETE_COURSE_ADMIN,

    VIEW_DECKS_VIA_CATEGORY,
    VIEW_DECKS_VIA_COURSE,
    VIEW_ONE_DECK_ADMIN,
    VIEW_DECKS_ADMIN,
    START_LEARNING_DECK_VIA_COURSE,
    START_LEARNING_DECK_VIA_CATEGORY,
    CREATE_DECK_IN_CATEGORY,
    CREATE_DECK_IN_COURSE,
    EDIT_DECK,
    DELETE_DECK_ADMIN,
    DELETE_DECK,
    DELETE_DECK_FROM_COURSE,

    CREATE_DECK_ADMIN,
    EDIT_DECK_ADMIN,

    DELETE_DECK_USER,
    CREATE_DECK_USER,
    EDIT_DECK_USER,
    VIEW_DECKS_USER,
    VIEW_ONE_DECK_USER,

    CREATE_COMMENT_FOR_DECK,
    EDIT_COMMENT_FOR_DECK,
    DELETE_COMMENT_FOR_DECK,
    VIEW_COMMENT_FOR_DECK,
    VIEW_ALL_COMMENTS_FOR_DECK,

    CREATE_COMMENT_FOR_COURSE,
    EDIT_COMMENT_FOR_COURSE,
    DELETE_COMMENT_FOR_COURSE,
    VIEW_COMMENT_FOR_COURSE,
    VIEW_ALL_COMMENTS_FOR_COURSE,

    EDIT_PERSONAL_DATA,
    CHANGE_PASSWORD,
    UPLOAD_PROFILE_IMAGE,
    DELETE_PROFILE_IMAGE,
    DELETE_PROFILE,

    SET_POINTS
}
