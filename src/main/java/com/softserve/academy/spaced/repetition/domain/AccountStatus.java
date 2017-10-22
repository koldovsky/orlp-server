package com.softserve.academy.spaced.repetition.domain;


public enum AccountStatus {
    INACTIVE, ACTIVE, BLOCKED, DELETED;

    public boolean isInactive() {
        return this.equals(INACTIVE);
    }

    public boolean isNotActive() {
        return !this.equals(ACTIVE);
    }

    public boolean isBlocked() {
        return this.equals(BLOCKED);
    }

    public boolean isDeleted() {
        return this.equals(DELETED);
    }
}
