package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeckService {
    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Deck> getAllDecks(Long course_id) {
        Course course = courseRepository.findOne(course_id);
        return course.getDecks();
    }

    public List<Deck> getAllDecksByCategory(Long category_id) {
        Category category = categoryRepository.findOne(category_id);
        return category.getDecks();
    }


    public List<Deck> getAllOrderedDecks() {
        return deckRepository.findAllByOrderByRatingDesc();
    }

    @Transactional
    public Deck getDeck(Long deck_id) {
        return deckRepository.findOne(deck_id);
    }

    public List<Card> getAllCardsByDeckId(Long deck_id) {
        Deck deck = deckRepository.findOne(deck_id);
        return deck.getCards();
    }

    @Transactional
    public void addDeckToCategory(Deck deck, Long category_id) {
        Category category = categoryRepository.findOne(category_id);
        category.getDecks().add(deckRepository.save(deck));
    }

    @Transactional
    public void addDeckToCourse(Deck deck, Long category_id, Long course_id) {
        Category category = categoryRepository.findOne(category_id);
        Course course = courseRepository.findOne(course_id);
        course.getDecks().add(deckRepository.save(deck));
    }

    public void updateDeck(Deck deck, Long deck_id) {
        deck.setId(deck_id);
        deckRepository.save(deck);
    }

    public void deleteDeck(Long deck_id) {
        deckRepository.delete(deck_id);
    }
}
