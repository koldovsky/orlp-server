package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.CardDTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.service.CardService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoForEntity;
import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoListForCollection;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("api/decks/{deckId}/")
public class CardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private CardService cardService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "cards")
    @PreAuthorize("hasPermission('CARD','READ')")
    public List<CardPublicDTO> getCardsByDeck(@PathVariable Long deckId) {
        return DTOBuilder.buildDtoListForCollection(cardService.findAllByDeckId(deckId), CardPublicDTO.class,
                linkTo(methodOn(CardController.class).getCardsByDeck(deckId)).withSelfRel());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("learn")
    @PreAuthorize("hasPermission('CARD','READ')")
    public List<CardPublicDTO> getLearningCards(@PathVariable Long deckId) {
        return buildDtoListForCollection(cardService.getLearningCards(deckId),
                CardPublicDTO.class, linkTo(methodOn(CardController.class).getCardsByDeck(deckId)).withSelfRel());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("learn/additional")
    @PreAuthorize("hasPermission('CARD','READ')")
    public List<CardPublicDTO> getAdditionalLearningCards(@PathVariable Long deckId)
            throws NotAuthorisedUserException {
        return buildDtoListForCollection(cardService.getAdditionalLearningCards(deckId), CardPublicDTO.class,
                linkTo(methodOn(CardController.class).getCardsByDeck(deckId)).withSelfRel());
    }

    @GetMapping("not-postponed")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('CARD','READ') && isAuthenticated()")
    public Boolean areThereNotPostponedCardsAvailable(@PathVariable Long deckId)
            throws NotAuthorisedUserException {
        return cardService.areThereNotPostponedCardsAvailable(deckId);
    }

    @Auditable(action = AuditingAction.CREATE_CARD_VIA_CATEGORY_AND_DECK)
    @PostMapping(value = "cards")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission('CARD','CREATE')")
    public CardPublicDTO addCard(@PathVariable Long deckId, @Validated @RequestBody CardDTO card) {
        LOGGER.debug("Adding card to deck with Id: {}", deckId);
        Card newCard = new Card(card.getTitle(), card.getQuestion(), card.getAnswer());
        cardService.addCard(newCard, deckId, card.getImages());
        return buildDtoForEntity(newCard, CardPublicDTO.class,
                linkTo(methodOn(CardController.class).getCardById(deckId, newCard.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.EDIT_CARD_VIA_CATEGORY_AND_DECK)
    @PutMapping(value = "cards/{cardId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('ADMIN_CARD','UPDATE') || (hasPermission('CARD','UPDATE') &&" +
            "@cardServiceImpl.getCard(#cardId).createdBy==principal.id)")
    public CardPublicDTO updateCard(@PathVariable Long deckId,
                                    @PathVariable Long cardId,
                                    @Validated @RequestBody CardDTO card) {
        LOGGER.debug("Updating card with id: {}  in deck with id: {}", cardId, deckId);
        Card newCard = cardService.updateCard(new Card(card.getTitle(), card.getQuestion(),
                card.getAnswer()), cardId, card.getImages());
        return buildDtoForEntity(newCard, CardPublicDTO.class,
                linkTo(methodOn(CardController.class).getCardById(deckId, cardId)).withSelfRel());
    }

    @Auditable(action = AuditingAction.DELETE_CARD)
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "cards/{cardId}")
    @PreAuthorize("hasPermission('ADMIN_CARD','DELETE') || " +
            "(hasPermission('CARD','DELETE') && @cardServiceImpl.getCard(#cardId).createdBy==principal.id)")
    public void deleteCard(@PathVariable Long cardId) {
        LOGGER.debug("Deleting card with id: {}", cardId);
        cardService.deleteCard(cardId);
    }

    @GetMapping(value = "cards/{cardId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('ADMIN_CARD','READ') || (hasPermission('CARD','READ') && " +
            "@cardServiceImpl.getCard(#cardId).createdBy==principal.id)")
    public CardPublicDTO getCardById(@PathVariable Long deckId, @PathVariable Long cardId) {
        Card card = cardService.getCard(cardId);
        return buildDtoForEntity(card, CardPublicDTO.class,
                linkTo(methodOn(CardController.class).getCardById(deckId, cardId)).withSelfRel());
    }
}
