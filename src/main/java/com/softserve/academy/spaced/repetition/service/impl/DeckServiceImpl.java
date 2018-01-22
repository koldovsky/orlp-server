package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.*;

import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.service.DeckService;
import com.softserve.academy.spaced.repetition.service.FolderService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DeckServiceImpl implements DeckService {
    private final static int QUANTITY_ADMIN_DECKS_IN_PAGE = 20;
    private final static int QUANTITY_DECKS_IN_PAGE = 12;
    private final static String DECK_EXCEPTION_MESSAGE = "Such deck not found";

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<Deck> getAllDecksByCourseId(Long courseId) {
        Course course = courseRepository.findOne(courseId);
        return course.getDecks();
    }

    @Override
    public List<Deck> getAllDecksByCategoryId(Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        return category.getDecks();
    }

    @Override
    public List<Deck> getAllDecksOrderedDescByRating() {
        return deckRepository.findAllByOrderByRatingDesc();
    }

    @Override
    @Transactional
    public Deck getDeckById(Long deckId) {
        return deckRepository.findOne(deckId);
    }

    @Override
    public List<Card> getAllCardsByDeckId(Long deckId) {
        Deck deck = deckRepository.findOne(deckId);
        return deck.getCards();
    }

    @Override
    @Transactional
    public void addDeckToCategory(Deck deck, Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        List<Deck> decks = category.getDecks();
        decks.add(deck);
    }

    @Override
    @Transactional
    public void addDeckToCourse(Deck deck, Long categoryId, Long courseId) {
        Course course = courseRepository.findOne(courseId);
        List<Deck> decks = course.getDecks();
        decks.add(deck);
    }

    @Override
    @Transactional
    public void updateDeck(Deck updatedDeck, Long deckId, Long categoryId) {
        Deck deck = deckRepository.findOne(deckId);
        Category category = categoryRepository.findById(categoryId);
        String name = updatedDeck.getName();
        String description = updatedDeck.getDescription();
        deck.setName(name);
        deck.setDescription(description);
        deck.setCategory(category);
    }

    @Override
    @Transactional
    public Deck updateDeckAdmin(Deck updatedDeck, Long deckId) {
        Deck deck = deckRepository.findOne(deckId);
        Category category = updatedDeck.getCategory();
        Long categoryId = category.getId();
        category = categoryRepository.findById(categoryId);
        String name = updatedDeck.getName();
        String description = updatedDeck.getDescription();
        deck.setName(name);
        deck.setDescription(description);
        deck.setCategory(category);
        return deck;
    }

    @Override
    @Transactional
    public void deleteDeckById(Long deckId) {
        deckRepository.deleteDeckById(deckId);
    }

    @Override
    @Transactional
    public void createNewDeck(Deck deck, Long categoryId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Category category = categoryRepository.findOne(categoryId);
        deck.setCategory(category);
        deck.setDeckOwner(user);
        deckRepository.save(deck);
    }

    @Override
    @Transactional
    public Deck createNewDeckAdmin(Deck deck) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Deck newDeck = new Deck();
        String name = deck.getName();
        String description = deck.getDescription();
        Category category = deck.getCategory();
        Long categoryId = category.getId();
        category = categoryRepository.findById(categoryId);
        newDeck.setName(name);
        newDeck.setDescription(description);
        newDeck.setCategory(category);
        newDeck.setDeckOwner(user);
        return deckRepository.save(newDeck);
    }

    @Override
    @Transactional
    public void deleteOwnDeck(Long deckId) throws NotAuthorisedUserException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.findOne(deckId);
        if (deck == null) {
            throw new NoSuchElementException(DECK_EXCEPTION_MESSAGE);
        }
        User deckOwner = deck.getDeckOwner();
        Long deckOwnerId = deckOwner.getId();
        Long userId = user.getId();
        if (deckOwnerId.equals(userId)) {
            deckRepository.delete(deck);
        } else {
            throw new NotOwnerOperationException();
        }
    }

    @Override
    @Transactional
    public Deck updateOwnDeck(Deck updatedDeck, Long deckId, Long categoryId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.findOne(deckId);
        if (deck == null) {
            throw new NoSuchElementException(DECK_EXCEPTION_MESSAGE);
        }
        User deckOwner = deck.getDeckOwner();
        Long deckOwnerId = deckOwner.getId();
        Long userId = user.getId();
        if (deckOwnerId.equals(userId)) {
            String name = updatedDeck.getName();
            String description = updatedDeck.getDescription();
            Category category = categoryRepository.findOne(categoryId);
            String syntaxToHighlight = updatedDeck.getSynthaxToHighlight();
            deck.setName(name);
            deck.setDescription(description);
            deck.setCategory(category);
            deck.setSynthaxToHighlight(syntaxToHighlight);
            return deck;
        } else {
            throw new NotOwnerOperationException();
        }
    }

    @Override
    public List<Deck> getAllUserDecks() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Long userId = user.getId();
        return deckRepository.findAllByDeckOwnerIdEquals(userId);
    }

    @Override
    public Deck getUserDeckByDeckId(Long deckId) throws NotAuthorisedUserException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.findOne(deckId);
        if (deck == null) {
            throw new NoSuchElementException(DECK_EXCEPTION_MESSAGE);
        }
        User deckOwner = deck.getDeckOwner();
        Long deckOwnerId = deckOwner.getId();
        Long userId = user.getId();
        if (deckOwnerId.equals(userId)) {
            return deck;
        } else {
            throw new NotOwnerOperationException();
        }
    }

    @Override
    public Page<Deck> getPageWithDecksByCategory(long categoryId, int pageNumber, String sortBy, boolean ascending) {
        Category category = categoryRepository.findOne(categoryId);
        PageRequest request = new PageRequest(--pageNumber, QUANTITY_DECKS_IN_PAGE,
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return deckRepository.findAllByCategoryEquals(category, request);
    }

    @Override
    public Page<Deck> getPageWithAllAdminDecks(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(--pageNumber, QUANTITY_ADMIN_DECKS_IN_PAGE,
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return deckRepository.findAll(request);
    }

    @Override
    public String getSyntaxToHighlight(long deckId){
        Deck deck = deckRepository.getDeckById(deckId);
        return deck.getSynthaxToHighlight();
    }
}
