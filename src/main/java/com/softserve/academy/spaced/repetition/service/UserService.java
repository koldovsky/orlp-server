package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeckRepository deckRepository;

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByAccount_Email(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User toggleUsersStatus(Long id, AccountStatus status) {

        User user = userRepository.findOne(id);

        if (user.getAccount().getStatus() == status) {
            user.getAccount().setStatus(AccountStatus.ACTIVE);
        } else if (user.getAccount().getStatus() == AccountStatus.ACTIVE) {
            user.getAccount().setStatus(status);
        }

        userRepository.save(user);
        return userRepository.findOne(id);
    }


    public User getUserById(Long userId) {
        return userRepository.findOne(userId);
    }

    public User addExistingDeckToUsersFolder(Long userId, Long deckId) {

        User user = userRepository.findOne(userId);
        Folder usersFolder = user.getFolder();


        for (Deck deck : usersFolder.getDecks()) {
            if (deck.getId() == deckId) {
                return null;
            }
        }

        Deck deckForAdding = deckRepository.findOne(deckId);
        usersFolder.getDecks().add(deckForAdding);
        userRepository.save(user);

        return user;
    }
}
