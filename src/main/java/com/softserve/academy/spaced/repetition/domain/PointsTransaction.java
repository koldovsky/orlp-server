package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "points_transaction")
public class PointsTransaction extends Transaction implements EntityInterface {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_from")
    private User userFrom;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_to")
    private User userTo;

    @Column(name = "points")
    private int points;

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}


