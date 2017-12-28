package com.softserve.academy.spaced.repetition.controller.utils.dto.impl;

import com.softserve.academy.spaced.repetition.controller.utils.dto.DTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import org.springframework.hateoas.Link;


public class CardPublicDTO extends DTO<Card> {

    public CardPublicDTO(Card card, Link link) {
        super(card, link);
    }

    public Long getCardId() { return getEntity().getId(); }

    public String getAnswer() {
        return getEntity().getAnswer();
    }

    public String getQuestion() { return getEntity().getQuestion();}

    public String getTitle() { return getEntity().getTitle(); }

    public double getRating(){
        return getEntity().getRating();
    }

}
