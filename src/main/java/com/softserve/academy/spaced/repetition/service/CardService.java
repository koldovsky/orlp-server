package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.utils.exceptions.EmptyFileException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.utils.exceptions.WrongFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;

/**
 * This interface works with cards.
 */
public interface CardService {
    /**
     * Returns cards that belong deck with the given identifier depends on learning regime.
     *
     * @param deckId must not be {@literal null}.
     * @return all cards with the given deck's id (by default 10)
     */
    List<Card> getLearningCards(Long deckId);

    /**
     * Retrieves an card with the given identifier.
     *
     * @param cardId must not be {@literal null}.
     * @return the card with the given identifier
     */
    Card getCard(Long cardId);

    /**
     * Adds card to deck with the given identifier.
     *
     * @param card   added card,must not be {@literal null}.
     * @param deckId must not be {@literal null}.
     */
    void addCard(Card card, Long deckId, List<String> imageList);

    /**
     * Updates card with the given identifier.
     *
     * @param cardId must not be {@literal null}.
     * @param card   updated card, must not be {null}.
     */
    Card updateCard(Card card, Long cardId, List<String> imageList);

    /**
     * Returns cards for studying from deck with the given identifier.
     *
     * @param deckId must not be {@literal null}.
     * @return cards for studying (by default 10)
     * @throws NotAuthorisedUserException if user is not authorised
     */
    List<Card> getCardsQueue(long deckId) throws NotAuthorisedUserException;

    /**
     * Deletes card with the given identifier.
     *
     * @param cardId must not be {@literal null}.
     */
    void deleteCard(Long cardId);

    /**
     * Returns all postponed cards that belong deck with the given identifier.
     *
     * @param deckId must not be {@literal null}.
     * @return postponed cards
     * @throws NotAuthorisedUserException if user is not authorised
     */
    List<Card> getAdditionalLearningCards(Long deckId) throws NotAuthorisedUserException;

    /**
     * Checks on availability of new or postponed cards in deck with the given identifier.
     *
     * @param deckId must not be {@literal null}.
     * @return true if found
     * @throws NotAuthorisedUserException if user is not authorised
     */
    boolean areThereNotPostponedCardsAvailable(Long deckId) throws NotAuthorisedUserException;

    /**
     * Uploads cards from .yml file to deck with the given identifier.
     *
     * @param cardsFile expect .yml file and throw exception otherwise
     * @param deckId    must not be {@literal null}.
     * @throws WrongFormatException       if invalid format of file
     * @throws EmptyFileException         if file is empty
     * @throws NotOwnerOperationException if user can not do this operation because he has not this deck
     * @throws NotAuthorisedUserException if user is not authorised
     * @throws IOException                if failed or interrupted I/O operations
     */
    void uploadCards(MultipartFile cardsFile, Long deckId) throws WrongFormatException, EmptyFileException,
            NotOwnerOperationException, NotAuthorisedUserException, IOException;

    /**
     * Downloads cards from website in .yml format from deck with the given identifier.
     *
     * @param deckId       must not be {@literal null}.
     * @param outputStream write date to file
     */
    void downloadCards(Long deckId, OutputStream outputStream);

    /**
     * Download template cards from file which stored ../orlp-server/src/main/resources/data/CardsTemplate.yml
     *
     * @param outputStream write date to file
     */
    void downloadCardsTemplate(OutputStream outputStream);

    List<Card> findAllByDeckId(Long deckId);
}
