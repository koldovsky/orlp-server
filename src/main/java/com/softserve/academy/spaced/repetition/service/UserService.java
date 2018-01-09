package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.PasswordDTO;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.impl.ImageServiceImpl;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * This interface proceeds all operations with users.
 */
public interface UserService {

    void setAuthorityRepository(AuthorityRepository authorityRepository);

    void setUserRepository(UserRepository userRepository);

    void setDeckRepository(DeckRepository deckRepository);

    void setPasswordEncoder(PasswordEncoder passwordEncoder);

    void setImageService(ImageServiceImpl imageService);

    void setMailService(MailService mailService);

    /**
     * Adds new user
     *
     * @param user which will be saved.
     */
    void addUser(User user);

    /**
     * Finds users by email.
     *
     * @param email user`s email which will be searched.
     * @return user that was found.
     * @deprecated newer used in project.
     */
    User findUserByEmail(String email);

    /**
     * Gets all users from entity User.
     *
     * @return list of users.
     * @deprecated newer used in project.
     */
    List<User> getAllUsers();

    /**
     * Sets user-status as active.
     *
     * @param id user`s id which will be set as active.
     * @return user tat was set as active.
     */
    User setUsersStatusActive(Long id);

    /**
     * Sets user-status as deleted.
     *
     * @param id user`s id which will be set as deleted.
     * @return user tat was set as deleted.
     */
    User setUsersStatusDeleted(Long id);

    /**
     * Sets user-status as blocked.
     *
     * @param id user`s id which will be set as blocked.
     * @return user tat was set as blocked.
     */
    User setUsersStatusBlocked(Long id);

    /**
     * Finds users by identifier.
     *
     * @param userId user`s id which will be searched.
     * @return user that was found.
     */
    User getUserById(Long userId);

    /**
     * Ads deck to users folder
     *
     * @param userId user`s id
     * @param deckId deck`s id
     * @return managed by admin user.
     */
    User addExistingDeckToUsersFolder(Long userId, Long deckId);

    /**
     *
     *
     *
     *
     *
     *
     */

    String getNoAuthenticatedUserEmail() throws NotAuthorisedUserException;

    /**
     * Gets user from authorised user.
     *
     * @return user from entity.
     * @throws NotAuthorisedUserException if user is unauthorised.
     */
    User getAuthorizedUser() throws NotAuthorisedUserException;

    /**
     * Gets all courses which was passed by identified user.
     *
     * @param userId user`s id.
     * @return Set of courses which was passed by identified user.
     */
    Set<Course> getAllCoursesByUserId(Long userId);

    /**
     * Deletes deck from users folder.
     *
     * @param userId users id.
     * @param deckId decks id.
     * @return managed by admin user.
     */
    User removeDeckFromUsersFolder(Long userId, Long deckId);

    /**
     * Gets list of decks from the folder of the defined user
     *
     * @param userId user`s id.
     * @return list managed by admin users.
     */
    List<Deck> getAllDecksFromUsersFolder(Long userId);

    /**
     * Gets page with all users.
     *
     * @param pageNumber page`s number.
     * @param sortBy     the properties to sort by, must not be null or empty.
     * @param ascending  the value that determines how the elements must be sorted on the page.
     * @return page with all users.
     */
    Page<User> getUsersByPage(int pageNumber, String sortBy, boolean ascending);

    /**
     * Sets new personal data.
     *
     * @param person new personal data that will be set.
     * @return managed by admin user.
     * @throws NotAuthorisedUserException if unauthorized user sets new personal data.
     */
    User editPersonalData(Person person) throws NotAuthorisedUserException;

    /**
     * Sets new password.
     *
     * @param passwordDTO new password.
     * @throws NotAuthorisedUserException if unauthorized user sets new password.
     */
    void changePassword(PasswordDTO passwordDTO) throws NotAuthorisedUserException;

    /**
     * Uploads users profile image.
     *
     * @param file image which will be set.
     * @return managed by admin user.
     * @throws ImageRepositorySizeQuotaExceededException image size is to large.
     * @throws NotAuthorisedUserException                unauthorized user sets image.
     */

    User uploadImage(MultipartFile file) throws ImageRepositorySizeQuotaExceededException,
            NotAuthorisedUserException;

    /**
     * Gets byte array of profile image.
     *
     * @return byte array of profile image.
     * @throws NotAuthorisedUserException unauthorized user gets profile image.
     */
    byte[] getDecodedImageContent() throws NotAuthorisedUserException;

    /**
     * Activates new accounts.
     *
     * @throws NotAuthorisedUserException unauthorized user used this operation.
     */
    void activateAccount() throws NotAuthorisedUserException;

    /**
     * Deletes user`s account.
     *
     * @throws NotAuthorisedUserException unauthorized user used this operation.
     */
    void deleteAccount() throws NotAuthorisedUserException;

    /**
     * Gets the status of the authorised user
     *
     * @throws UserStatusException
     */
    void getUserStatus() throws UserStatusException;

    /**
     * Initializes a new user.
     *
     * @param account            user`s account.
     * @param email              user`s email.
     * @param accountStatus      user`s status.
     * @param deactivated
     * @param authenticationType
     */
    void initializeNewUser(Account account, String email, AccountStatus accountStatus,
                           boolean deactivated, AuthenticationType authenticationType);

    /**
     * Checks user`s status.
     *
     * @param user user which will be checked.
     * @throws UserStatusException if user is no active.
     */
    void isUserStatusActive(User user) throws UserStatusException;
}
