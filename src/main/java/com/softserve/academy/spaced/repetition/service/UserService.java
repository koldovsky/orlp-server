package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.dto.impl.PasswordDTO;
import com.softserve.academy.spaced.repetition.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.UserStatusException;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.impl.ImageServiceImpl;
import com.softserve.academy.spaced.repetition.service.impl.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {
    @Autowired
    void setAuthorityRepository(AuthorityRepository authorityRepository);

    @Autowired
    void setUserRepository(UserRepository userRepository);

    @Autowired
    void setDeckRepository(DeckRepository deckRepository);

    @Autowired
    void setPasswordEncoder(PasswordEncoder passwordEncoder);

    @Autowired
    void setImageService(ImageServiceImpl imageService);

    @Autowired
    void setMailService(MailServiceImpl mailService);

    void addUser(User user);

    User findUserByEmail(String email);

    List<User> getAllUsers();

    User setUsersStatusActive(Long id);

    User setUsersStatusDeleted(Long id);

    User setUsersStatusBlocked(Long id);

    User getUserById(Long userId);

    User addExistingDeckToUsersFolder(Long userId, Long deckId);

    String getNoAuthenticatedUserEmail() throws NotAuthorisedUserException;

    User getAuthorizedUser() throws NotAuthorisedUserException;

    Set<Course> getAllCoursesByUserId(Long user_id);

    User removeDeckFromUsersFolder(Long userId, Long deckId);

    List<Deck> getAllDecksFromUsersFolder(Long userId);

    Page<User> getUsersByPage(int pageNumber, String sortBy, boolean ascending);

    @Transactional
    User editPersonalData(Person person) throws NotAuthorisedUserException;

    @Transactional
    void changePassword(PasswordDTO passwordDTO) throws NotAuthorisedUserException;

    User uploadImage(MultipartFile file) throws ImageRepositorySizeQuotaExceededException,
            NotAuthorisedUserException;

    byte[] getDecodedImageContent() throws NotAuthorisedUserException;

    void activateAccount() throws NotAuthorisedUserException;

    void deleteAccount() throws NotAuthorisedUserException;

    void getUserStatus() throws UserStatusException;

    @Transactional
    void initializeNewUser(Account account, String email, AccountStatus accountStatus, boolean deactivated, AuthenticationType
            authenticationType);

    void isUserStatusActive(User user) throws UserStatusException;
}
