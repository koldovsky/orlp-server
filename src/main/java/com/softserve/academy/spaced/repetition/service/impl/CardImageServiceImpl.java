package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.CardImage;
import com.softserve.academy.spaced.repetition.repository.CardImageRepository;
import com.softserve.academy.spaced.repetition.service.CardImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardImageServiceImpl implements CardImageService{

    @Autowired
    private CardImageRepository cardImageRepository;

    @Override
    public CardImage save(CardImage cardImage) {
        return cardImageRepository.save(cardImage);
    }

    @Override
    public CardImage findOne(long id) {
        return cardImageRepository.findOne(id);
    }
}
