package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;

import com.softserve.academy.spaced.repetition.exceptions.NoSuchDeckException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void deleteDeck(Long deckId) {
        List<Course> courses = courseRepository.findAll();
        List<Deck> decks;
        for(Course course: courses){
            decks=course.getDecks();
            for(Deck deck : decks){
                if (deck.getId().equals(deckId)) {
                    decks.remove(deck);
                    break;
                }
            }
            courseRepository.save(course);
        }
        deckRepository.deleteDeckById(deckId);
    }

    @Transactional
    public void createNewDeck(Deck newDeck, Long category_id) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Deck deck = new Deck();
        deck.setName(newDeck.getName());
        deck.setDescription(newDeck.getDescription());
        deck.setCategory(categoryRepository.findById(category_id));
        deck.setDeckOwner(user);
        Deck save = deckRepository.save(deck);
        newDeck.setId(save.getId());
    }

    @Transactional
    public void deleteOwnDeck(Long deckId)
            throws NotAuthorisedUserException, NoSuchDeckException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.getDeckById(deckId);
        if(deck==null)  throw new NoSuchDeckException();
        if(deck.getDeckOwner().getId().equals( user.getId() )) {
            folderService.deleteDeckFromAllUsers(deckId);
            deleteDeck(deckId);
        }
        else throw new NotOwnerOperationException();
    }

    @Transactional
    public void updateOwnDeck(Deck updatedDeck, Long deckId, Long categoryId) throws NotAuthorisedUserException, NoSuchDeckException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.getDeckById(deckId);
        if(deck==null)  throw new NoSuchDeckException();
        if(deck.getDeckOwner().getId().equals( user.getId() )) {
            deck.setName(updatedDeck.getName());
            deck.setDescription(updatedDeck.getDescription());
            deck.setCategory(categoryRepository.findById(categoryId));
            deckRepository.save(deck);
         }
        else throw new NotOwnerOperationException();
    }

    public List<Deck> getAllDecksByUser() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        return deckRepository.findAllByDeckOwner_IdEquals(user.getId());
    }

    public Deck getDeckUser(Long deckId) throws NotAuthorisedUserException, NoSuchDeckException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.getDeckById(deckId);
        if(deck==null) throw new NoSuchDeckException();;
        if(deck.getDeckOwner().getId().equals( user.getId() )) return deck;
        else throw new NotOwnerOperationException();
    }

}
