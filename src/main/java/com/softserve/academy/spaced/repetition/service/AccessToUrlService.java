package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("AccessToUrlService")
@Service
public class AccessToUrlService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DeckRepository deckRepository;

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
}
