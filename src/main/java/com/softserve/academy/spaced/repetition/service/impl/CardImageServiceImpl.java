package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardImage;
import com.softserve.academy.spaced.repetition.repository.CardImageRepository;
import com.softserve.academy.spaced.repetition.service.CardImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardImageServiceImpl implements CardImageService {

    @Autowired
    private CardImageRepository cardImageRepository;

    @Override
    public void addCardImage(List<String> imageList, Card card) {
        if (imageList != null) {
            imageList.forEach(image -> cardImageRepository.save(new CardImage(image, card)));
        }
    }

    @Override
    public void deleteById(Long cardImageID) {
        cardImageRepository.delete(cardImageID);
    }
}
