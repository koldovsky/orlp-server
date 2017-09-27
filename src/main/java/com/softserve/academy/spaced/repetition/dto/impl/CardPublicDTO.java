package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.controller.UserCardQueueController;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


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
