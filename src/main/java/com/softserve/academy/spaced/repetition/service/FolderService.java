package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.FolderRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeckRepository deckRepository;


    public Deck addDeck(Long deckId) {

        Deck deck = deckRepository.getDeckById(deckId);

        User user = userService.getAuthorizedUser();

        Folder folder = user.getFolder();
        List <Deck> decks = folder.getDecks();
        decks.add(deck);
        folderRepository.save(folder);

        return deck;
    }

    public List <Deck> getAllDecksByFolderId(Long folder_id) {
        Folder folder = folderRepository.findOne(folder_id);

        return folder.getDecks();
    }

}

