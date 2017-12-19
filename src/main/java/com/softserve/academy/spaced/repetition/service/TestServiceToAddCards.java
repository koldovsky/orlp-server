package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TestServiceToAddCards {
    @Transactional
    void addDeck(List<Card> cards);
}
