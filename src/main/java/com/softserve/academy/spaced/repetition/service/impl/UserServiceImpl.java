package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.config.PointsBalanceEvent;
import com.softserve.academy.spaced.repetition.controller.dto.impl.AddPointsByAdminDTO;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.*;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.PointsTransactionRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.authentification.JwtUser;
import com.softserve.academy.spaced.repetition.service.ImageService;
import com.softserve.academy.spaced.repetition.service.MailService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.PasswordCannotBeNullException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import com.softserve.academy.spaced.repetition.utils.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.DataBinder;

import java.util.*;

import static com.softserve.academy.spaced.repetition.domain.Account.INITIAL_CARDS_NUMBER;

@Service
public class UserServiceImpl implements UserService {

    private final Locale locale = LocaleContextHolder.getLocale();
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

    @Autowired
    private PointsTransactionRepository transactionRepository;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private MessageSource messageSource;

    int QUANTITY_USER_IN_PAGE = 20;

    @Autowired
    private ApplicationEventPublisher publisher;

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
        try {
            account.setEmail(email);
            if (account.getPassword() != null) {
                account.setPassword(passwordEncoder.encode(account.getPassword()));
            }
            account.setAuthenticationType(authenticationType);
            validateAccount(account);
            account.setLastPasswordResetDate(new Date());
            account.setStatus(accountStatus);
            account.setAuthenticationType(authenticationType);
            account.setDeactivated(deactivated);
            Authority authority = authorityRepository.findAuthorityByName(AuthorityName.ROLE_USER);
            account.setAuthorities(Collections.singleton(authority));
            account.setLearningRegime(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
            account.setCardsNumber(INITIAL_CARDS_NUMBER);
        } catch (PasswordCannotBeNullException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void isUserStatusActive(User user) throws UserStatusException {
        if (user.getAccount().getStatus().isNotActive()) {
            throw new UserStatusException(user.getAccount().getStatus());
        }
    }

    @Override
    public User updatePointsBalance(User user) {
        Integer expenses = Optional.ofNullable(transactionRepository.getAllExpensesByUser(user.getId())).orElse(0);
        Integer income = Optional.ofNullable(transactionRepository.getAllIncomeByUser(user.getId())).orElse(0);
        user.setPoints(income - expenses);
        return userRepository.save(user);
    }

    @Override
    public boolean isAdmin(User user) throws NotAuthorisedUserException {
        return getAuthorizedUser().getAccount().getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equals(AuthorityName.ROLE_ADMIN));
    }

    @Override
    @Transactional
    public AddPointsByAdminDTO addPointsToUser(AddPointsByAdminDTO addPointsByAdminDTO) throws NotAuthorisedUserException {
        User admin = getAuthorizedUser();
        User user = userRepository.findUserByAccountEmail(addPointsByAdminDTO.getEmail());
        PointsTransaction pointsTransaction = new PointsTransaction(admin, user, addPointsByAdminDTO.getPoints(), TransactionType.TRANSFER);
        pointsTransaction.setCreationDate(new Date());
        pointsTransaction.setReference(pointsTransaction);
        transactionRepository.save(pointsTransaction);
        publisher.publishEvent(new PointsBalanceEvent(this.getClass().getCanonicalName(), user));
        addPointsByAdminDTO.setPoints(user.getPoints());
        return addPointsByAdminDTO;
    }

    /**
     * Validates whether user's account can or cannot contain a null password value. If user registered
     * via Facebook or Google then it allows for password field to be null, but will throw
     * PasswordCannotBeNullException if user's authentication type is LOCAL and password is null.
     *
     * @param account            user`s account.
     * @throws PasswordCannotBeNullException if authenticationType is LOCAL and password is null
     */
    private void validateAccount(Account account) throws PasswordCannotBeNullException {
        DataBinder binder = new DataBinder(account);
        binder.setValidator(passwordValidator);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            throw new PasswordCannotBeNullException(messageSource
                    .getMessage("message.validation.fieldNotNull", new Object[]{}, locale));
        }
    }
}
