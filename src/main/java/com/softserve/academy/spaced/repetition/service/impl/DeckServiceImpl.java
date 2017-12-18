package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.*;

import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.repository.*;
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
public class DeckServiceImpl implements com.softserve.academy.spaced.repetition.service.DeckService {
    private final static int QUANTITY_ADMIN_DECKS_IN_PAGE = 20;
    private final static int QUANTITY_DECKS_IN_PAGE = 12;
    private final static String DECK_EXCEPTION_MESSAGE = "Such deck not found";

    private DeckRepository deckRepository;

    private CategoryRepository categoryRepository;

    private CardRepository cardRepository;

    private CourseRepository courseRepository;

    private UserService userService;

    private FolderServiceImpl folderServiceImpl;

    @Autowired
    public void setDeckRepository(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setCardRepository(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Autowired
    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setFolderServiceImpl(FolderServiceImpl folderServiceImpl) {
        this.folderServiceImpl = folderServiceImpl;
    }

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

    @Override
    @Transactional
    public void addDeckToCategory(Deck deck, Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        category.getDecks().add(deckRepository.save(deck));
    }

    @Override
    @Transactional
    public void addDeckToCourse(Deck deck, Long categoryId, Long courseId) {
        Course course = courseRepository.findOne(courseId);
        course.getDecks().add(deckRepository.save(deck));
    }

    @Override
    @Transactional
    public void updateDeck(Deck updatedDeck, Long deckId, Long categoryId) {
        Deck deck = deckRepository.findOne(deckId);
        deck.setName(updatedDeck.getName());
        deck.setDescription(updatedDeck.getDescription());
        deck.setCategory(categoryRepository.findById(categoryId));
        deckRepository.save(deck);
    }

    @Override
    @Transactional
    public Deck updateDeckAdmin(Deck updatedDeck, Long deckId) {
        Deck deck = deckRepository.findOne(deckId);
        deck.setName(updatedDeck.getName());
        deck.setDescription(updatedDeck.getDescription());
        deck.setCategory(categoryRepository.findById(updatedDeck.getCategory().getId()));
        return deckRepository.save(deck);
    }

    @Override
    @Transactional
    public void deleteDeck(Long deckId) {
        deckRepository.deleteDeckById(deckId);
    }

    @Override
    @Transactional
    public void createNewDeck(Deck newDeck, Long categoryId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        newDeck.setCategory(categoryRepository.findOne(categoryId));
        newDeck.setDeckOwner(user);
        deckRepository.save(newDeck);
    }

    @Override
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

    @Override
    @Transactional
    public void deleteOwnDeck(Long deckId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.findOne(deckId);
        if (deck == null) {
            throw new NoSuchElementException(DECK_EXCEPTION_MESSAGE);
        }
        if (deck.getDeckOwner().getId().equals(user.getId())) {
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
        if (deck.getDeckOwner().getId().equals(user.getId())) {
            deck.setName(updatedDeck.getName());
            deck.setDescription(updatedDeck.getDescription());
            deck.setCategory(categoryRepository.findOne(categoryId));
            deck.setSynthaxToHighlight(updatedDeck.getSynthaxToHighlight());
            return deckRepository.save(deck);
        } else {
            throw new NotOwnerOperationException();
        }
    }

    @Override
    public List<Deck> getAllDecksByUser() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        return deckRepository.findAllByDeckOwner_IdEquals(user.getId());
    }

    @Override
    public Deck getDeckUser(Long deckId) throws NotAuthorisedUserException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.findOne(deckId);
        if (deck == null) {
            throw new NoSuchElementException(DECK_EXCEPTION_MESSAGE);
        }
        if (deck.getDeckOwner().getId().equals(user.getId())) {
            return deck;
        } else {
            throw new NotOwnerOperationException();
        }
    }

    @Override
    public Page<Deck> getPageWithDecksByCategory(long categoryId, int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_DECKS_IN_PAGE, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return deckRepository.findAllByCategoryEquals(categoryRepository.findOne(categoryId), request);
    }

    @Override
    public Page<Deck> getPageWithAllAdminDecks(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_ADMIN_DECKS_IN_PAGE, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return deckRepository.findAll(request);
    }

    @Override
    public String getSynthaxToHightlight(long deckId) {
        return deckRepository.getDeckById(deckId).getSynthaxToHighlight();
    }
}
