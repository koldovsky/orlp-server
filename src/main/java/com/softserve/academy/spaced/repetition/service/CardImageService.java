package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;

import java.util.List;

public interface CardImageService {

    void addCardImage(List<String> imageList, Card card);

    void deleteById(Long cardImageID);
}
