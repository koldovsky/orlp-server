package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardImage;
import com.softserve.academy.spaced.repetition.repository.CardImageRepository;
import com.softserve.academy.spaced.repetition.service.CardImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardImageServiceImpl implements CardImageService{

    private static final String BASE64_TYPE = "base64";

    @Autowired
    private CardImageRepository cardImageRepository;

    @Override
    public void save(List<String> imageList, Card card) {
        if(imageList != null) {
            if (imageList.get(0).endsWith(BASE64_TYPE))
                cardImageRepository.save(new CardImage(imageList.get(0) + "," + imageList.get(1), card));
            else
                imageList.forEach(image -> cardImageRepository.save(new CardImage(image, card)));
        }
    }

    @Override
    public void delete(Long cardImageID) {
        cardImageRepository.delete(cardImageID);
    }
}
