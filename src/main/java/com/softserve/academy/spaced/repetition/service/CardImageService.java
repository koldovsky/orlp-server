package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardImage;

import java.util.List;

public interface CardImageService {

    void save(List<String> imageList, Card card);

    void delete(Long cardImageID);
}
