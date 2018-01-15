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

public interface CardService {
    List<Card> getLearningCards(Long deckId) throws NotAuthorisedUserException;

    Card getCard(Long id);

    void addCard(Card card, Long deckId, List<MultipartFile> imageList);

    void updateCard(Card card, Long cardId, List<MultipartFile> imageList);

    List<Card> getCardsQueue(long deckId) throws NotAuthorisedUserException;

    void deleteCard(Long cardId);

    List<Card> getAdditionalLearningCards(Long deckId) throws NotAuthorisedUserException;

    boolean areThereNotPostponedCardsAvailable(Long deckId) throws NotAuthorisedUserException;

    void uploadCards(MultipartFile cardsFile, Long deckId)
            throws WrongFormatException, EmptyFileException, NotOwnerOperationException,
            NotAuthorisedUserException, IOException;

    void downloadCards(Long deckId, OutputStream outputStream);

    void downloadCardsTemplate(OutputStream outputStream);

}
