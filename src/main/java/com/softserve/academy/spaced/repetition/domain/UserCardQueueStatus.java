package com.softserve.academy.spaced.repetition.domain;

public enum UserCardQueueStatus {
    GOOD("GOOD"), NORMAL("NORMAL"), BAD("BAD");
    public String status;

    UserCardQueueStatus(String status) {
        this.status = status;
    }
}
