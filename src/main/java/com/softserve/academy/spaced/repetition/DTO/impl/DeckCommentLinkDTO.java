package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.controller.CourseCommentController;
import com.softserve.academy.spaced.repetition.controller.DeckCommentController;
import com.softserve.academy.spaced.repetition.domain.DeckComment;
import org.springframework.hateoas.Link;

import java.util.Date;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class DeckCommentLinkDTO extends DTO<DeckComment> {

    public DeckCommentLinkDTO(DeckComment commentForDeck, Link link) {
        super(commentForDeck, link);
        add(linkTo(methodOn(DeckCommentController.class).getAllCommentsForDeck(getEntity().getDeck().getCategory()
                .getId(), getEntity().getDeck().getId())).withRel("comments"));
    }

    public Long getCommentId(){return getEntity().getId();}

    public String getCommentText() {return getEntity().getCommentText();}

    public Date getCommentCreateDate() {return getEntity().getCommentDate();}

    public String getPersonFirstName(){return getEntity().getPerson().getFirstName();}

    public String getPersonLastName(){return getEntity().getPerson().getLastName();}
}
