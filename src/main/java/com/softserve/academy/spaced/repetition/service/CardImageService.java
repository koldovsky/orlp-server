package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.CardImage;

public interface CardImageService {

    CardImage save(CardImage cardImage);

    CardImage findOne(long id);
}
