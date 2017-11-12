package com.softserve.academy.spaced.repetition.domain;


public enum AccountStatus {
    INACTIVE, ACTIVE, BLOCKED, DELETED, INACTIVE_BLOCKED;

    public boolean isNotActive() {
        return !this.equals(ACTIVE);
    }
}
