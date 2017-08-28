package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.DTO.impl.DeckPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.FolderRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeckRepository deckRepository;

    public Deck addDeck(Long deckId) throws NotAuthorisedUserException {

        Deck deck = deckRepository.getDeckById(deckId);

        User user = userService.getAuthorizedUser();

        Folder folder = user.getFolder();
        Set<Deck> decks = folder.getDecks();
        decks.add(deck);
        folderRepository.save(folder);

        return deck;
    }

    public List<Deck> getAllDecksByFolderId(Long folder_id) {
        Folder folder = folderRepository.findOne(folder_id);
        List<Deck> decks = new ArrayList<>(folder.getDecks());

        return decks;
    }

    public List<Long> getAllDecksIdWithFolder() throws NotAuthorisedUserException {
        User authorizedUser = userService.getAuthorizedUser();
        Long folderId = authorizedUser.getFolder().getId();

        return folderRepository.selectAllDeckIdWithFolder(folderId);
    }
}
