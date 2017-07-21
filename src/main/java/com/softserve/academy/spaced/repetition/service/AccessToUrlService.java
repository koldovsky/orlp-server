package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
@Component("accessToUrlService")
public class AccessToUrlService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CardRepository cardRepository;

    public boolean hasAccessToCourse(Long category_id, Long course_id) {
        return courseRepository.getAccessToCourse(category_id, course_id).size() > 0;
    }

    public boolean hasAccessToCourse(Long category_id) {
        return courseRepository.getAccessToCourse(category_id).size() > 0;
    }

    public boolean hasAccessToDeck(Long category_id, Long course_id, Long deck_id) {
        return hasAccessToCourse(category_id, course_id) & (deckRepository.hasAccessToDeck(course_id, deck_id).size() > 0);
    }

    public boolean hasAccessToDeckFromCategory(Long category_id, Long deck_id) {
        return deckRepository.hasAccessToDeckFromCategory(category_id, deck_id).size() > 0;
    }

    public boolean hasAccessToDeck(Long category_id) {
        return deckRepository.hasAccessToDeckFromCategory(category_id).size() > 0;
    }

    public boolean hasAccessToCard(Long category_id, Long deck_id, Long card_id) {
        return hasAccessToDeckFromCategory(category_id, deck_id) & (cardRepository.hasAccessToCard(deck_id, card_id).size() > 0);
    }

    public boolean hasAccessToCard(Long category_id, Long course_id, Long deck_id, Long card_id) {
        return hasAccessToDeck(category_id, course_id, deck_id) & (cardRepository.hasAccessToCard(deck_id, card_id).size() > 0);
    }
}
