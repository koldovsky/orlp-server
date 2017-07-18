package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Folder;
import org.springframework.hateoas.Link;

import java.util.List;

public class FolderPublicDTO extends DTO<Folder> {

    public FolderPublicDTO(Folder folder, Link link) {
        super(folder, link);
    }

    public List<Deck> getDecks(){
        return getEntity().getDecks();
    }

}
