package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.DTO.CardPublic;


public class CardPublicDTO extends Card implements CardPublic {
    public CardPublicDTO() {
    }

    public CardPublicDTO(Long id, String question, String answer) {
        super(id, question, answer);
    }

    public CardPublicDTO(Card card) {
        super(card.getId(), card.getQuestion(), card.getAnswer());
    }
}
