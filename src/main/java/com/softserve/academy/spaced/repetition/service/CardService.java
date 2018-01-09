package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.utils.exceptions.EmptyFileException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.utils.exceptions.WrongFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
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
     * @throws NotAuthorisedUserException
     */
    List<Card> getLearningCards(Long deckId) throws NotAuthorisedUserException;

    /**
     * Retrieves an card by its id.
     *
     * @param id must not be {@literal null}.
     * @return the card with the given id or {@literal null} if none found.
     */
    Card getCard(Long id);

    /**
     * Adds card to deck with the given identifier.
     *
     * @param card   must not be {null}.
     * @param deckId must not be {@literal null}.
     */
    void addCard(Card card, Long deckId);

    /**
     * Updates card with the given identifier.
     *
     * @param id   must not be {@literal null}.
     * @param card must not be {null}.
     */
    void updateCard(Long id, Card card);

    /**
     * Returns cards for studying from deck with the given identifier.
     *
     * @param deckId must not be {@literal null}.
     * @return cards for studying (by default 10)
     * @throws NotAuthorisedUserException
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
     * @throws NotAuthorisedUserException
     */
    List<Card> getAdditionalLearningCards(Long deckId) throws NotAuthorisedUserException;

    /**
     * Checks on availability of new or postponed cards in deck with the given identifier.
     *
     * @param deckId must not be {@literal null}.
     * @return true if found
     * @throws NotAuthorisedUserException
     */
    boolean areThereNotPostponedCardsAvailable(Long deckId) throws NotAuthorisedUserException;

    /**
     * Uploads cards from .yml file to deck with the given identifier.
     *
     * @param cardsFile expect .yml file and throw exception otherwise
     * @param deckId    must not be {@literal null}.
     * @throws WrongFormatException
     * @throws EmptyFileException
     * @throws NotOwnerOperationException
     * @throws NotAuthorisedUserException
     * @throws IOException
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

}
