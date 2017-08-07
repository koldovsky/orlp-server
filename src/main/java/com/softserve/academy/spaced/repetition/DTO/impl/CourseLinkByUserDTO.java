//package com.softserve.academy.spaced.repetition.DTO.impl;
//
//
//import com.softserve.academy.spaced.repetition.DTO.DTO;
//import com.softserve.academy.spaced.repetition.controller.DeckController;
//import com.softserve.academy.spaced.repetition.controller.FolderController;
//import com.softserve.academy.spaced.repetition.controller.authorization.UserController;
//import com.softserve.academy.spaced.repetition.domain.Course;
//import com.softserve.academy.spaced.repetition.domain.Deck;
//import org.springframework.hateoas.Link;
//
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
//
//public class CourseLinkByUserDTO extends DTO<Course> {
//        public DeckLinkByFolderDTO(Deck deck, Link link) {
//            super(course, link);
//
//            Link linkCards = linkTo(methodOn(UserController.class).getCardsByFolderAndDeck((long) -1, getEntity().getId())).withRel("cards");
//            add(getLinkWithReplacedParentPart(linkCards).withRel("cards"));
//        }
//
//        public String getName() { return getEntity().getName(); }
//
//        public String getDescription() { return getEntity().getDescription(); }
//    }
//
//
//
//    public CourseLinkByUserDTO(Course course, Link link) {
//        super(course, link);
//        add(linkTo(methodOn(DeckController.class).getAllDecksByCourseId(getEntity().getCategory().getId(), getEntity().getId())).withRel("decks"));
//    }
//    Link linkCourses = linkTo(methodOn(FolderController.class).getCardsByFolderAndDeck((long) -1, getEntity().getId())).withRel("cards");
//    public String getName() {
//        return getEntity().getName();
//    }
//
//    public String getDescription() {
//        return getEntity().getDescription();
//    }
//
//    public String getImagebase64() {
//        return getEntity().getImagebase64();
//    }
//}}
