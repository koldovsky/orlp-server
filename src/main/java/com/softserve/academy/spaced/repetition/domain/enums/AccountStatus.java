package com.softserve.academy.spaced.repetition.domain.enums;


public enum AccountStatus {
    ACTIVE, BLOCKED, DELETED;

    public boolean isNotActive() {
        return !this.equals(ACTIVE);
    }
}
