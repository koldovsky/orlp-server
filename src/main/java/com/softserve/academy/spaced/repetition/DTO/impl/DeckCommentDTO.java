package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.DeckComment;
import org.springframework.hateoas.Link;

import java.util.Date;

public class DeckCommentDTO extends DTO<DeckComment> {

    public DeckCommentDTO(DeckComment commentForDeck, Link link) {
        super(commentForDeck, link);
    }

    public Long getCommentId(){return getEntity().getId();}

    public String getCommentText() {return getEntity().getCommentText();}

    public Date getCommentCreateDate() {return getEntity().getCommentDate();}

    public String getPersonFirstName(){return getEntity().getPerson().getFirstName();}

    public String getPersonLastName(){return getEntity().getPerson().getLastName();}
}
