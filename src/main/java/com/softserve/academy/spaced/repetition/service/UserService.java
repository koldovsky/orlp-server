package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.controller.dto.impl.PasswordDTO;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.impl.ImageServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {

    void setAuthorityRepository(AuthorityRepository authorityRepository);

    void setUserRepository(UserRepository userRepository);

    void setDeckRepository(DeckRepository deckRepository);

    void setPasswordEncoder(PasswordEncoder passwordEncoder);

    void setImageService(ImageServiceImpl imageService);

    void setMailService(MailService mailService);

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

    Set<Course> getAllCoursesByUserId(Long userId);

    User removeDeckFromUsersFolder(Long userId, Long deckId);

    List<Deck> getAllDecksFromUsersFolder(Long userId);

    Page<User> getUsersByPage(int pageNumber, String sortBy, boolean ascending);

    User editPersonalData(Person person) throws NotAuthorisedUserException;

    void changePassword(PasswordDTO passwordDTO) throws NotAuthorisedUserException;

    User uploadImage(MultipartFile file) throws ImageRepositorySizeQuotaExceededException,
            NotAuthorisedUserException;

    byte[] getDecodedImageContent() throws NotAuthorisedUserException;

    void activateAccount() throws NotAuthorisedUserException;

    void deleteAccount() throws NotAuthorisedUserException;

    void getUserStatus() throws UserStatusException;

    void initializeNewUser(Account account, String email, AccountStatus accountStatus,
                           boolean deactivated, AuthenticationType authenticationType);

    void isUserStatusActive(User user) throws UserStatusException;
}
