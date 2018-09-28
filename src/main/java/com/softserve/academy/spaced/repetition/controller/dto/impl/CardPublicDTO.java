package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.CardController;
import com.softserve.academy.spaced.repetition.controller.CardImageController;
import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardImage;
import org.springframework.hateoas.Link;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


public class CardPublicDTO extends DTO<Card> {

    public CardPublicDTO(Card card, Link link) {
        super(card, link);
    }

    public Long getCardId() {
        return getEntity().getId();
    }

    public String getAnswer() {
        return getEntity().getAnswer();
    }

    public String getQuestion() {
        return getEntity().getQuestion();
    }

    public String getTitle() {
        return getEntity().getTitle();
    }

    public double getRating() {
        return getEntity().getRating();
    }

    public List<CardImage> getCardImages() {
        return getEntity().getCardImages();
    }

    public Long getCreatedBy(){
        return getEntity().getCreatedBy();
    }

}
