package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;

import com.softserve.academy.spaced.repetition.exceptions.NoSuchDeckException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeckService {
    private final static int QUANTITY_ADMIN_DECKS_IN_PAGE = 20;
    private final static int QUANTITY_DECKS_IN_PAGE = 12;
    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FolderService folderService;

    public List<Deck> getAllDecks(Long courseId) {
        Course course = courseRepository.findOne(courseId);
        return course.getDecks();
    }

    public List<Deck> getAllDecksByCategory(Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        return category.getDecks();
    }

    public List<Deck> getAllOrderedDecks() {
        return deckRepository.findAllByOrderByRatingDesc();
    }

    @Transactional
    public Deck getDeck(Long deckId) {
        return deckRepository.findOne(deckId);
    }

    public List<Card> getAllCardsByDeckId(Long deckId) {
        Deck deck = deckRepository.findOne(deckId);
        return deck.getCards();
    }

    @Transactional
    public void addDeckToCategory(Deck deck, Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        category.getDecks().add(deckRepository.save(deck));
    }

    @Transactional
    public void addDeckToCourse(Deck deck, Long categoryId, Long courseId) {
        Category category = categoryRepository.findOne(categoryId);
        Course course = courseRepository.findOne(courseId);
        course.getDecks().add(deckRepository.save(deck));
    }

    @Transactional
    public void updateDeck(Deck updatedDeck, Long deckId, Long categoryId) {
        Deck deck = deckRepository.findOne(deckId);
        deck.setName(updatedDeck.getName());
        deck.setDescription(updatedDeck.getDescription());
        deck.setCategory(categoryRepository.findById(categoryId));
        deckRepository.save(deck);
    }

    @Transactional
    public Deck  updateDeckAdmin(Deck updatedDeck, Long deckId) {
        Deck deck = deckRepository.findOne(deckId);
        deck.setName(updatedDeck.getName());
        deck.setDescription(updatedDeck.getDescription());
        deck.setCategory(categoryRepository.findById(updatedDeck.getCategory().getId()));
        return deckRepository.save(deck);
    }

    @Transactional
    public void deleteDeck(Long deckId) {
        deckRepository.deleteDeckById(deckId);
    }

    @Transactional
    public void createNewDeck(Deck newDeck, Long category_id) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        newDeck.setCategory(categoryRepository.findOne(category_id));
        newDeck.setDeckOwner(user);
        deckRepository.save(newDeck);
    }

    @Transactional
    public Deck createNewDeckAdmin(Deck newDeck) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Deck deck = new Deck();
        deck.setName(newDeck.getName());
        deck.setDescription(newDeck.getDescription());
        deck.setCategory(categoryRepository.findById(newDeck.getCategory().getId()));
        deck.setDeckOwner(user);
        Deck savedDeck = deckRepository.save(deck);
        deck.setId(savedDeck.getId());
        return savedDeck;
    }

    @Transactional
    public void deleteOwnDeck(Long deckId)
            throws NotAuthorisedUserException, NoSuchDeckException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.findOne(deckId);
        if (deck == null) {
            throw new NoSuchDeckException();
        }
        if (deck.getDeckOwner().getId().equals(user.getId())) {
            deckRepository.delete(deck);
        } else {
            throw new NotOwnerOperationException();
        }
    }

    @Transactional
    public Deck updateOwnDeck(Deck updatedDeck, Long deckId, Long categoryId)
            throws NotAuthorisedUserException, NoSuchDeckException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.findOne(deckId);
        if (deck == null) {
            throw new NoSuchDeckException();
        }
        if (deck.getDeckOwner().getId().equals(user.getId())) {
            deck.setName(updatedDeck.getName());
            deck.setDescription(updatedDeck.getDescription());
            deck.setCategory(categoryRepository.findOne(categoryId));
            return deckRepository.save(deck);
        } else {
            throw new NotOwnerOperationException();
        }
    }

    public List<Deck> getAllDecksByUser() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        return deckRepository.findAllByDeckOwner_IdEquals(user.getId());
    }

    public Deck getDeckUser(Long deckId) throws NotAuthorisedUserException, NoSuchDeckException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.findOne(deckId);
        if (deck == null) {
            throw new NoSuchDeckException();
        }
        if (deck.getDeckOwner().getId().equals(user.getId())) {
            return deck;
        } else {
            throw new NotOwnerOperationException();
        }
    }

    public Page<Deck> getPageWithDecksByCategory(long categoryId, int pageNumber, String sortBy, boolean ascending) {
        PageRequest request;
        if(ascending == true){
            request = new PageRequest(pageNumber-1, QUANTITY_DECKS_IN_PAGE, Sort.Direction.ASC, sortBy);
        }else {
            request = new PageRequest(pageNumber-1, QUANTITY_DECKS_IN_PAGE, Sort.Direction.DESC, sortBy);
        }
        return deckRepository.findAllByCategoryEquals(categoryRepository.findOne(categoryId), request);
    }

    public Page<Deck> getPageWithAllAdminDecks(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request;
        if(ascending == true){
            request = new PageRequest(pageNumber-1, QUANTITY_ADMIN_DECKS_IN_PAGE, Sort.Direction.ASC, sortBy);
        }else {
            request = new PageRequest(pageNumber-1, QUANTITY_ADMIN_DECKS_IN_PAGE, Sort.Direction.DESC, sortBy);
        }
        return deckRepository.findAll(request);
    }
}
