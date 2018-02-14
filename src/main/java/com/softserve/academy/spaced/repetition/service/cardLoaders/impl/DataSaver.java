package com.softserve.academy.spaced.repetition.service.cardLoaders.impl;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class DataSaver {
    @Autowired
    private CardService cardService;
    @Autowired
    private CardRepository cardRepository;

    public void save(Map<String, String> map, Long deckId) {
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> elem = iterator.next();
            if (ifCarExistsInDb(elem.getKey())) {
                Card card = new Card();
                card.setAnswer(elem.getKey());
                card.setQuestion(elem.getValue());
                cardService.addCard(card, deckId, null);
            }
        }
    }

    private boolean ifCarExistsInDb(String question) {
        List<Card> questionsInDb = cardRepository.findAllByQuestion(question);
        if (questionsInDb.isEmpty()) {
            return true;
        }
        return false;
    }
}
