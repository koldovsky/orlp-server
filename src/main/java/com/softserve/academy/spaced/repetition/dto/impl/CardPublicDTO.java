package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import org.springframework.hateoas.Link;

import java.util.List;


public class CardPublicDTO extends DTO<Card> {
    public CardPublicDTO(Card card) {
        super(card);
    }

    public String getAnswer(){
        return getEntity().getAnswer();
    }

    public String getQuestion(){
        return  getEntity().getQuestion();
    }

    public List<Link> getLinks(){
        return getEntity().getLinks();
    }
}
