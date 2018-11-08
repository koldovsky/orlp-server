package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "deck_price")
public class DeckPrice implements EntityInterface {

































    @Override
    public Long getId() {
        return null;
    }
}
