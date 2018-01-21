package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
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
    public Deck addDeckToFolderById(Long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.getDeckById(deckId);
        Folder folder = user.getFolder();
        Set<Deck> decks = folder.getDecks();
        decks.add(deck);
        folderRepository.save(folder);
        return deck;
    }

    @Override
    public List<Deck> getAllDecksByFolderId(Long folderId) {
        Folder folder = folderRepository.findOne(folderId);
        Set<Deck> decks = folder.getDecks();
        return new ArrayList<>(decks);
    }

    @Override
    public List<Long> getAllDecksIdFromFolder() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Folder folder = user.getFolder();
        Long folderId = folder.getId();
        return folderRepository.selectAllDecksIdFromFolder(folderId);
    }

    @Override
    public void deleteDeckById(Long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Folder folder = user.getFolder();
        Set<Deck> decks = folder.getDecks();
        for (Deck deck: decks) {
            if (deck.getId() == deckId) {
                decks.remove(deck);
                break;
            }
        }
        folderRepository.save(folder);
    }

    @Override
    @Transactional
    public void deleteDeckFromAllUsersFolderById(Long deckId) {
        List<Folder> folders = folderRepository.getAllFolderWhereIdDecksEquals(deckId);
        for (Folder folder : folders) {
            Set<Deck> decks = folder.getDecks();
            for (Deck deck : decks) {
                if (deck.getId().equals(deckId)) {
                    decks.remove(deck);
                    break;
                }
            }
            folderRepository.save(folder);
        }
    }
}
