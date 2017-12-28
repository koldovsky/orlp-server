package com.softserve.academy.spaced.repetition.controller.utils.dto.impl;

import com.softserve.academy.spaced.repetition.controller.utils.dto.DTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Folder;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

public class FolderPublicDTO extends DTO<Folder> {

    public FolderPublicDTO(Folder folder, Link link) {
        super(folder, link);
    }

    public List<Deck> getDecks(){
        List<Deck> decks = new ArrayList<>(getEntity().getDecks());

        return decks;
    }

}
