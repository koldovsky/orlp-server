package com.softserve.academy.spaced.repetition.config;

import com.softserve.academy.spaced.repetition.domain.User;
import org.springframework.context.ApplicationEvent;

public class PointsBalanceEvent extends ApplicationEvent {

    private User user;

    public PointsBalanceEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
