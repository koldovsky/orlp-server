package com.softserve.academy.spaced.repetition.domain;

public enum UserCardQueueStatus {
    GOOD("GOOD"), NORMAL("NORMAL"), BAD("BAD");

    private String status;

    public String getStatus() {
        return status;
    }

    UserCardQueueStatus(String status) {
        this.status = status;
    }
}
