package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardImage;
import com.softserve.academy.spaced.repetition.repository.CardImageRepository;
import com.softserve.academy.spaced.repetition.service.impl.CardImageServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class CardImageServiceTest {

    @Mock
    private CardImageRepository cardImageRepository;
    @InjectMocks
    private CardImageServiceImpl cardImageService;
    private Card card;
    private CardImage cardImage;
    private List<String> oneImageList;
    private List<String> imageList;

    @Before
    public void setUp() {
        final Long CARD_ID = 1l;
        oneImageList = new ArrayList<>();
        oneImageList.add("base64");
        oneImageList.add("imageInBase64");
        imageList = new ArrayList<>();
        imageList.add("imageInBase64");
        imageList.add("imageInBase64");
        card = DomainFactory.createCard(CARD_ID, "Card One",
                "What are the supported platforms by Java Programming Language?",
                "Java runs on a variety of platforms, such as Windows, Mac OS, and the\n" +
                        "    various versions of UNIX/Linux like HP-Unix, Sun Solaris, Redhat Linux, Ubuntu,\n" +
                        "    CentOS, etc.", null);
        cardImage = DomainFactory.createCardImage(1l, "very long string", card);
    }

    @Test
    public void testSaveWithOneImages() {
        when(cardImageRepository.save(cardImage)).thenReturn(cardImage);

        cardImageService.addCardImage(oneImageList, card);
        verify(cardImageRepository).save(any(CardImage.class));
    }

    @Test
    public void testSaveWithManyImages() {
        when(cardImageRepository.save(new CardImage(imageList.get(0), card))).thenReturn(cardImage);

        cardImageService.addCardImage(imageList, card);
        verify(cardImageRepository, times(2)).save(any(CardImage.class));
    }

}
