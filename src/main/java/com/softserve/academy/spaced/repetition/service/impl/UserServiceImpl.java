package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.*;
import com.softserve.academy.spaced.repetition.service.ImageService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.authentification.JwtUser;
import com.softserve.academy.spaced.repetition.service.MailService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.softserve.academy.spaced.repetition.domain.Account.INITIAL_CARDS_NUMBER;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageService imageService;

    @Autowired
    private MailService mailService;

    @Autowired
    private AuthorityRepository authorityRepository;

    int QUANTITY_USER_IN_PAGE = 20;

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByAccountEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User setUsersStatusActive(Long id) {
        User user = userRepository.findOne(id);
        user.getAccount().setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        return userRepository.findOne(id);
    }

    @Override
    public User setUsersStatusDeleted(Long id) {
        User user = userRepository.findOne(id);
        user.getAccount().setStatus(AccountStatus.DELETED);
        userRepository.save(user);
        return userRepository.findOne(id);
    }

    @Override
    public User setUsersStatusBlocked(Long id) {
        User user = userRepository.findOne(id);
        user.getAccount().setStatus(AccountStatus.BLOCKED);
        userRepository.save(user);
        return userRepository.findOne(id);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public User addExistingDeckToUsersFolder(Long userId, Long deckId) {
        User user = userRepository.findOne(userId);
        Folder usersFolder = user.getFolder();
        for (Deck deck : usersFolder.getDecks()) {
            if (deck.getId().equals(deckId)) {
                return null;
            }
        }
        Deck deckForAdding = deckRepository.findOne(deckId);
        usersFolder.getDecks().add(deckForAdding);
        userRepository.save(user);
        return user;
    }
    //TODO: Move to separate class
    @Override
    public String getNoAuthenticatedUserEmail() throws NotAuthorisedUserException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof JwtUser) {
            JwtUser jwtUser = (JwtUser) principal;
            return jwtUser.getUsername();
        } else {
            throw new NotAuthorisedUserException();
        }
    }
    //TODO: Move to separate class
    @Override
    public User getAuthorizedUser() throws NotAuthorisedUserException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            throw new NotAuthorisedUserException();
        } else {
            JwtUser jwtUser = (JwtUser) principal;
            return userRepository.findUserByAccountEmail(jwtUser.getUsername());
        }
    }

    @Override
    public Set<Course> getAllCoursesByUserId(Long userId) {
        User user = userRepository.findOne(userId);
        return user.getCourses();
    }

    @Override
    public User removeDeckFromUsersFolder(Long userId, Long deckId) {
        Deck deck = deckRepository.getDeckByItsIdAndOwnerOfDeck(deckId, userId);
        User user = userRepository.findOne(userId);
        Folder usersFolder = user.getFolder();
        boolean hasFolderDeck = false;
        for (Deck deckFromUsersFolder : usersFolder.getDecks()) {
            if (deckFromUsersFolder.getId().equals(deckId)) {
                hasFolderDeck = true;
            }
        }
        if (deck == null && hasFolderDeck == true) {
            deck = deckRepository.findOne(deckId);
            usersFolder.getDecks().remove(deck);
            userRepository.save(user);
        } else {
            return null;
        }
        return user;
    }

    @Override
    public List<Deck> getAllDecksFromUsersFolder(Long userId) {
        User user = userRepository.findOne(userId);
        Folder usersFolder = user.getFolder();
        List<Deck> decks = new ArrayList<>();
        decks.addAll(usersFolder.getDecks());
        return decks;
    }

    @Override
    public Page<User> getUsersByPage(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_USER_IN_PAGE,
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return userRepository.findAll(request);
    }

    @Override
    public void activateAccount() throws NotAuthorisedUserException {
        mailService.sendActivationMail(getNoAuthenticatedUserEmail());
    }

    @Override
    public void deleteAccount() throws NotAuthorisedUserException {
        User user = getAuthorizedUser();
        user.getAccount().setStatus(AccountStatus.DELETED);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void initializeNewUser(Account account, String email, AccountStatus accountStatus,
                                  boolean deactivated, AuthenticationType authenticationType) {
        account.setEmail(email);
        if (account.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        account.setLastPasswordResetDate(new Date());
        account.setStatus(accountStatus);
        account.setAuthenticationType(authenticationType);
        account.setDeactivated(deactivated);
        Authority authority = authorityRepository.findAuthorityByName(AuthorityName.ROLE_USER);
        account.setAuthorities(Collections.singleton(authority));
        account.setLearningRegime(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        account.setCardsNumber(INITIAL_CARDS_NUMBER);
    }

    @Override
    public void isUserStatusActive(User user) throws UserStatusException {
        if (user.getAccount().getStatus().isNotActive()) {
            throw new UserStatusException(user.getAccount().getStatus());
        }
    }
}
