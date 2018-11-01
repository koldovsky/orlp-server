package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckCreateValidationDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckEditByAdminDTO;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.DeckService;
import com.softserve.academy.spaced.repetition.service.FolderService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class DeckServiceImpl implements DeckService {
    private static final int QUANTITY_ADMIN_DECKS_IN_PAGE = 20;
    private static final int QUANTITY_DECKS_IN_PAGE = 12;

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

    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    @Override
    public List<Deck> getAllDecks(Long courseId) {
        Course course = courseRepository.findOne(courseId);
        return course.getDecks();
    }

    @Override
    public List<Deck> getAllDecksByCategory(Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        return category.getDecks();
    }

    @Override
    public List<Deck> getAllOrderedDecks() {
        return deckRepository.findAllByHiddenFalseOrderByRatingDesc();
    }

    @Override
    public Deck getDeck(Long deckId) {
        return deckRepository.findOne(deckId);
    }

    @Override
    public List<Card> getAllCardsByDeckId(Long deckId) {
        Deck deck = deckRepository.findOne(deckId);
        return deck.getCards();
    }

    @Override
    public Set<BigInteger> findDecksId(String searchString) {
        return deckRepository.findDecksId(searchString);
    }

    @Override
    @Transactional
    public void addDeckToCourse(Deck deck, Long courseId) {
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
    public Deck updateDeckAdmin(DeckEditByAdminDTO updatedDeck, Long deckId) {
        Deck deck = deckRepository.findOne(deckId);
        deck.setName(updatedDeck.getName());
        deck.setDescription(updatedDeck.getDescription());
        deck.setCategory(categoryRepository.findById(updatedDeck.getCategoryId()));
        return deckRepository.save(deck);
    }

    @Override
    @Transactional
    public void deleteDeck(Long deckId) {
        deckRepository.delete(deckId);
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
    public Deck createNewDeckAdmin(DeckCreateValidationDTO deckCreateValidationDTO) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Deck deck = new Deck();
        deck.setName(deckCreateValidationDTO.getName());
        deck.setDescription(deckCreateValidationDTO.getDescription());
        deck.setCategory(categoryRepository.findById(deckCreateValidationDTO.getCategoryId()));
        deck.setDeckOwner(user);
        Deck savedDeck = deckRepository.save(deck);
        deck.setId(savedDeck.getId());
        folderService.addDeck(deck.getId());
        return savedDeck;
    }

    @Override
    @Transactional
    public void deleteOwnDeck(Long deckId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.findOne(deckId);
        if (deck == null) {
            throw new NoSuchElementException(messageSource.getMessage("message.exception.deckNotFound",
                    new Object[]{}, locale));
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
            throw new NoSuchElementException(messageSource.getMessage("message.exception.deckNotFound",
                    new Object[]{}, locale));
        }
        if (deck.getDeckOwner().getId().equals(user.getId())) {
            deck.setName(updatedDeck.getName());
            deck.setDescription(updatedDeck.getDescription());
            deck.setCategory(categoryRepository.findOne(categoryId));
            deck.setSyntaxToHighlight(updatedDeck.getSyntaxToHighlight());
            return deckRepository.save(deck);
        } else {
            throw new NotOwnerOperationException();
        }
    }

    @Override
    public List<Deck> getAllDecksByUser() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        return deckRepository.findAllByDeckOwnerIdEquals(user.getId());
    }

    @Override
    public Deck getDeckUser(Long deckId) throws NotAuthorisedUserException, NotOwnerOperationException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.findOne(deckId);
        if (deck == null) {
            throw new NoSuchElementException(messageSource.getMessage("message.exception.deckNotFound",
                    new Object[]{}, locale));
        }
        if (deck.getDeckOwner().getId().equals(user.getId())) {
            return deck;
        } else {
            throw new NotOwnerOperationException();
        }
    }

    @Override
    public Deck toggleDeckAccess(Long deckId) {
        Deck deck = deckRepository.findOne(deckId);
        deck.setHidden(!deck.isHidden());
        deckRepository.save(deck);
        return deck;
    }

    @Override
    public Page<Deck> getPageWithDecksByCategory(long categoryId, int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_DECKS_IN_PAGE, ascending
                ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return deckRepository.findAllByCategoryEqualsAndHiddenFalse(categoryRepository.findOne(categoryId), request);
    }

    @Override
    public Page<Deck> getPageWithAllAdminDecks(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_ADMIN_DECKS_IN_PAGE,
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return deckRepository.findAll(request);
    }

    @Override
    public String getSynthaxToHightlight(long deckId) {
        return deckRepository.getDeckById(deckId).getSyntaxToHighlight();
    }

    @Override
    public List<Deck> findAllDecksBySearch(String searchString) {
        return deckRepository.findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(searchString, searchString);
    }
}
