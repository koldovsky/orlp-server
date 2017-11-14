package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.dto.impl.PasswordDTO;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.UserStatusException;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import com.softserve.academy.spaced.repetition.service.validators.DataFieldValidator;
import com.softserve.academy.spaced.repetition.service.validators.PasswordFieldValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.softserve.academy.spaced.repetition.domain.Account.INITIAL_CARDS_NUMBER;

@Service
public class UserService {

    public final static int QUANTITY_USER_IN_PAGE = 20;

    private UserRepository userRepository;

    private DeckRepository deckRepository;

    private PasswordEncoder passwordEncoder;

    private ImageService imageService;

    private MailService mailService;

    private DataFieldValidator dataFieldValidator;

    private PasswordFieldValidator passwordFieldValidator;

    private AuthorityRepository authorityRepository;

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setDeckRepository(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setDataFieldValidator(DataFieldValidator dataFieldValidator) {
        this.dataFieldValidator = dataFieldValidator;
    }

    @Autowired
    public void setPasswordFieldValidator(PasswordFieldValidator passwordFieldValidator) {
        this.passwordFieldValidator = passwordFieldValidator;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByAccountEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User setUsersStatusActive(Long id) {
        User user = userRepository.findOne(id);
        user.getAccount().setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        return userRepository.findOne(id);
    }

    public User setUsersStatusDeleted(Long id) {
        User user = userRepository.findOne(id);
        user.getAccount().setStatus(AccountStatus.DELETED);
        userRepository.save(user);
        return userRepository.findOne(id);
    }

    public User setUsersStatusBlocked(Long id) {
        User user = userRepository.findOne(id);
        user.getAccount().setStatus(AccountStatus.BLOCKED);
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
            if (deck.getId().equals(deckId)) {
                return null;
            }
        }
        Deck deckForAdding = deckRepository.findOne(deckId);
        usersFolder.getDecks().add(deckForAdding);
        userRepository.save(user);
        return user;
    }

    public User getAuthorizedUser() throws NotAuthorisedUserException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            throw new NotAuthorisedUserException();
        } else {
            JwtUser jwtUser = (JwtUser) principal;
            return userRepository.findUserByAccountEmail(jwtUser.getUsername());
        }
    }

    public Set<Course> getAllCoursesByUserId(Long user_id) {
        User user = userRepository.findOne(user_id);
        return user.getCourses();
    }

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

    public List<Deck> getAllDecksFromUsersFolder(Long userId) {
        User user = userRepository.findOne(userId);
        Folder usersFolder = user.getFolder();
        List<Deck> decks = new ArrayList<>();
        decks.addAll(usersFolder.getDecks());
        return decks;
    }

    public Page<User> getUsersByPage(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request;
        if (ascending == true) {
            request = new PageRequest(pageNumber - 1, QUANTITY_USER_IN_PAGE, Sort.Direction.ASC, sortBy);
        } else {
            request = new PageRequest(pageNumber - 1, QUANTITY_USER_IN_PAGE, Sort.Direction.DESC, sortBy);
        }
        return userRepository.findAll(request);
    }

    @Transactional
    public User editPersonalData(Person person) throws NotAuthorisedUserException {
        User user = getAuthorizedUser();
        dataFieldValidator.validate(person);
        user.getPerson().setFirstName(person.getFirstName());
        user.getPerson().setLastName(person.getLastName());
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(PasswordDTO passwordDTO) throws NotAuthorisedUserException {
        User user = getAuthorizedUser();
        passwordFieldValidator.validate(passwordDTO);
        user.getAccount().setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
        mailService.sendPasswordNotificationMail(user);
    }

    public User uploadImage(MultipartFile file) throws ImageRepositorySizeQuotaExceededException,
            NotAuthorisedUserException {
        imageService.checkImageExtention(file);
        User user = getAuthorizedUser();
        user.getPerson().setImageBase64(imageService.encodeToBase64(file));
        user.getPerson().setTypeImage(ImageType.BASE64);
        return userRepository.save(user);
    }

    public byte[] getDecodedImageContent() throws NotAuthorisedUserException {
        User user = getAuthorizedUser();
        String encodedFileContent = user.getPerson().getImageBase64();
        return imageService.decodeFromBase64(encodedFileContent);
    }

    public void activateAccount() throws NotAuthorisedUserException {
        User user = getAuthorizedUser();
        user.getAccount().setStatus(AccountStatus.ACTIVE);
        mailService.sendConfirmationMail(user);
        userRepository.save(user);
    }

    public void deleteAccount() throws NotAuthorisedUserException {
        User user = getAuthorizedUser();
        user.getAccount().setStatus(AccountStatus.INACTIVE);
        userRepository.save(user);
    }

    public void getUserStatus() throws UserStatusException {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByAccountEmail(jwtUser.getUsername());
        if (user.getAccount().getStatus().isNotActive()) {
            throw new UserStatusException(user.getAccount().getStatus());
        }
    }

    @Transactional
    public void initializeNewUser(Account account, String email, AccountStatus accountStatus, AuthenticationType
            authenticationType) {
        account.setEmail(email);
        if (account.getPassword() == null) {
            account.setPassword("-1");
        } else {
            passwordEncoder.encode(account.getPassword());
        }
        account.setLastPasswordResetDate(new Date());
        account.setStatus(accountStatus);
        account.setAuthenticationType(authenticationType);
        Authority authority = authorityRepository.findAuthorityByName(AuthorityName.ROLE_USER);
        account.setAuthorities(Collections.singleton(authority));
        account.setLearningRegime(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        account.setCardsNumber(INITIAL_CARDS_NUMBER);
    }

    public void isUserStatusActive(User user) throws UserStatusException {
        if (user.getAccount().getStatus().isNotActive()) {
            throw new UserStatusException(user.getAccount().getStatus());
        }
    }
}
