package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.FolderRepository;
import com.softserve.academy.spaced.repetition.service.FolderService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FolderServiceImpl implements FolderService{

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private DeckRepository deckRepository;

    @Override
    public Deck addDeck(Long deckId) throws NotAuthorisedUserException {

        Deck deck = deckRepository.getDeckById(deckId);

        User user = userService.getAuthorizedUser();

        Folder folder = user.getFolder();
        Set<Deck> decks = folder.getDecks();
        if (!decks.add(deck)) {
            folder.getDecks().remove(deck);
        }
        folderRepository.save(folder);

        return deck;
    }

    @Override
    public List<Deck> getAllDecksByFolderId(Long folder_id) {
        Folder folder = folderRepository.findOne(folder_id);
        List<Deck> decks = new ArrayList<>(folder.getDecks());

        return decks;
    }

    @Override
    public List<Long> getAllDecksIdWithFolder() throws NotAuthorisedUserException {
        User authorizedUser = userService.getAuthorizedUser();
        Long folderId = authorizedUser.getFolder().getId();

        return folderRepository.selectAllDeckIdWithFolder(folderId);
    }

    @Override
    public void deleteDeck(Long deck_id) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Folder folder = user.getFolder();
        Collection<Deck> userDecks = folder.getDecks();
        for (Deck deck: userDecks) {
            if (deck.getId() == deck_id) {
                userDecks.remove(deck);
                break;
            }
        }
        folderRepository.save(folder);
    }

    @Override
    @Transactional
    public void deleteDeckFromAllUsers(Long deck_id) throws NotAuthorisedUserException {
        List<Folder> folders = folderRepository.getAllFolderWhereIdDecksEquals(deck_id);
        if(folders.size()!=0) {
            for (Folder folder : folders) {
                Collection<Deck> folderDecks = folder.getDecks();
                for (Deck deck : folderDecks) {
                    if (deck.getId().equals(deck_id)) {
                        folderDecks.remove(deck);
                        break;
                    }
                }
                folderRepository.save(folder);
            }
        }
    }
}
