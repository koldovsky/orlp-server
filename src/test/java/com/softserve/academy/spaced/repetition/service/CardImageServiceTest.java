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
    private List<String> imageList;

    @Before
    public void setUp() {
        final Long CARD_ID = 1L;;
        imageList = new ArrayList<>();
        imageList.add("imageInBase64");
        imageList.add("imageInBase64");
        card = DomainFactory.createCard(CARD_ID, "Card One",
                "What are the supported platforms by Java Programming Language?",
                "Java runs on a variety of platforms, such as Windows, Mac OS, and the\n" +
                        "    various versions of UNIX/Linux like HP-Unix, Sun Solaris, Redhat Linux, Ubuntu,\n" +
                        "    CentOS, etc.", null);
        cardImage = DomainFactory.createCardImage(1L, "very long string", card);
    }

    @Test
    public void testSaveImages() {
        when(cardImageRepository.save(cardImage)).thenReturn(cardImage);

        cardImageService.addCardImage(imageList, card);
        verify(cardImageRepository, times(2)).save(any(CardImage.class));
    }
}
