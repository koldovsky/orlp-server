package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TopRatedDecksService {
    @Autowired
    private DeckRepository deckRepository;

    public List <Deck> getTopRatedDecks() {
        List <Deck> listOf4TopRatedDecks = new ArrayList();
        Iterator <Deck> iter = deckRepository.findTop4DecksOrderByDeckRating().iterator();
        int counter = 1;
        while (iter.hasNext() && counter <= 4) {
            listOf4TopRatedDecks.add(iter.next());
            counter++;
        }
        return listOf4TopRatedDecks;
    }
}
