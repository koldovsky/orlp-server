package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeckRepository deckRepository;

    @Transactional
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public User findUserByEmail(String email) {
        return userRepository.findUserByAccountEmail(email);
    }

    @Transactional
    public List <User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
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

    @Transactional
    public User getUserById(Long userId) {
        return userRepository.findOne(userId);
    }

    @Transactional
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

    public User getAuthorizedUser() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserByAccountEmail(jwtUser.getUsername());
    }

    public Set<Course> getAllCoursesByUserId(Long user_id) {
        User user = userRepository.findOne(user_id);
        return user.getCourses();
    }
}
