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

    private static final String BASE64_TYPE = "base64";
    private static final int MIN_IMAGE_LIST_SIZE = 2;

    @Autowired
    private CardImageRepository cardImageRepository;

    @Override
    public void addCardImage(List<String> imageList, Card card) {
        if (imageList != null && imageList.size() >= MIN_IMAGE_LIST_SIZE) {
            if (imageList.get(0).endsWith(BASE64_TYPE)) {
                cardImageRepository.save(new CardImage(imageList.get(0) + "," + imageList.get(1), card));
            } else {
                imageList.forEach(image -> cardImageRepository.save(new CardImage(image, card)));
            }
        }
    }

    @Override
    public void deleteById(Long cardImageID) {
        cardImageRepository.delete(cardImageID);
    }
}
