package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.PasswordDTO;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.*;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import com.softserve.academy.spaced.repetition.service.ImageService;
import com.softserve.academy.spaced.repetition.service.MailService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public User setUserStatusActive(Long userId) {
        User user = userRepository.findOne(userId);
        Account account = user.getAccount();
        account.setStatus(AccountStatus.ACTIVE);
        return user;
    }

    @Override
    public User setUserStatusDeleted(Long userId) {
        User user = userRepository.findOne(userId);
        Account account = user.getAccount();
        account.setStatus(AccountStatus.DELETED);
        return user;
    }

    @Override
    public User setUserStatusBlocked(Long userId) {
        User user = userRepository.findOne(userId);
        Account account = user.getAccount();
        account.setStatus(AccountStatus.BLOCKED);
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public User addExistingDeckToUserFolder(Long userId, Long deckId) {
        User user = userRepository.findOne(userId);
        Folder folder = user.getFolder();
        Set<Deck> decks = folder.getDecks();
        for (Deck deck : decks) {
            if (deck.getId().equals(deckId)) {
                return null;
            }
        }
        Deck deck = deckRepository.findOne(deckId);
        decks.add(deck);
        return user;
    }

    @Override
    public User getAuthorizedUser() throws NotAuthorisedUserException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            throw new NotAuthorisedUserException();
        } else {
            JwtUser jwtUser = (JwtUser) principal;
            String username = jwtUser.getUsername();
            return userRepository.findUserByAccountEmail(username);
        }
    }

    @Override
    public Set<Course> getAllCoursesByUserId(Long userId) {
        User user = userRepository.findOne(userId);
        return user.getCourses();
    }

    @Override
    public User removeDeckFromUserFolder(Long userId, Long deckId) {
        User user = userRepository.findOne(userId);
        Deck deck = deckRepository.findOne(deckId);
        Folder folder = user.getFolder();
        Set<Deck> decks = folder.getDecks();
        decks.remove(deck);
        return user;
    }

    @Override
    public List<Deck> getAllDecksFromUserFolderByUserId(Long userId) {
        User user = userRepository.findOne(userId);
        Folder folder = user.getFolder();
        Set<Deck> decks = folder.getDecks();
        List<Deck> deckList = new ArrayList<>(decks);
        return deckList;
    }

    @Override
    public Page<User> getUsersPageByPageNumber(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_USER_IN_PAGE,
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return userRepository.findAll(request);
    }

    @Override
    @Transactional
    public User editPersonalData(Person newPerson) throws NotAuthorisedUserException {
        User user = getAuthorizedUser();
        Person person = user.getPerson();
        String newFirstName = newPerson.getFirstName();
        String newLastName = newPerson.getLastName();
        person.setFirstName(newFirstName);
        person.setLastName(newLastName);
        return user;
    }

    @Override
    @Transactional
    public void changePassword(PasswordDTO passwordDTO) throws NotAuthorisedUserException {
        User user = getAuthorizedUser();
        Account account = user.getAccount();
        String newPassword = passwordDTO.getNewPassword();
        String encodedPassword = passwordEncoder.encode(newPassword);
        account.setPassword(encodedPassword);
        mailService.sendPasswordNotificationMail(user);
    }

    @Override
    public User uploadImage(MultipartFile file) throws ImageRepositorySizeQuotaExceededException,
            NotAuthorisedUserException {
        User user = getAuthorizedUser();
        imageService.checkImageExtension(file);
        Person person = user.getPerson();
        String imageBase64 = imageService.encodeToBase64(file);
        person.setImageBase64(imageBase64);
        person.setTypeImage(ImageType.BASE64);
        return user;
    }

    @Override
    public byte[] getDecodedImageContent() throws NotAuthorisedUserException {
        User user = getAuthorizedUser();
        Person person = user.getPerson();
        String imageBase64 = person.getImageBase64();
        return imageService.decodeFromBase64(imageBase64);
    }

    @Override
    public void activateAccount() throws NotAuthorisedUserException {
        mailService.sendActivationMail(getNotAuthenticatedUserEmail());
    }

    private String getNotAuthenticatedUserEmail() throws NotAuthorisedUserException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof JwtUser) {
            JwtUser jwtUser = (JwtUser) principal;
            return jwtUser.getUsername();
        } else {
            throw new NotAuthorisedUserException();
        }
    }

    @Override
    public void deleteAccount() throws NotAuthorisedUserException {
        User user = getAuthorizedUser();
        Account account = user.getAccount();
        account.setDeactivated(true);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void getUserStatus() throws UserStatusException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String username = jwtUser.getUsername();
        User user = userRepository.findUserByAccountEmail(username);
        Account account = user.getAccount();
        AccountStatus status = account.getStatus();
        if (status.isNotActive()) {
            throw new UserStatusException(status);
        }
    }

    @Override
    @Transactional
    public void initializeNewUser(Account account, String email, AccountStatus accountStatus,
                                  boolean deactivated, AuthenticationType authenticationType) {
        account.setEmail(email);
        if (account.getPassword() != null) {
            String password = account.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            account.setPassword(encodedPassword);
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
        Account account = user.getAccount();
        AccountStatus status = account.getStatus();
        if (status.isNotActive()) {
            throw new UserStatusException(status);
        }
    }
}
