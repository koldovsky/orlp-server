package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

/**
 * This interface proceeds all operations with users.
 */
public interface UserService {
    /**
     * Adds new user
     *
     * @param user which will be saved.
     */
    void addUser(User user);

    /**
     * Finds users by email.
     *
     * @param email user`s email by which user will be found.
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
     * @param id user`s id by which will be set as deleted.
     * @return user tat was set as deleted.
     */
    User setUsersStatusDeleted(Long id);

    /**
     * Sets user-status as blocked.
     *
     * @param id user`s id which will be set as blocked.
     * @return user that was set as blocked.
     */
    User setUsersStatusBlocked(Long id);

    /**
     * Finds user by identifier.
     *
     * @param userId user`s id which will be searched.
     * @return user that was found.
     */
    User getUserById(Long userId);

    /**
     * Adds deck to users folder
     *
     * @param userId user`s id
     * @param deckId deck`s id
     * @return user that was found by userId.
     */
    User addExistingDeckToUsersFolder(Long userId, Long deckId);

    /**
     * Gets the user`s email.
     *
     * @return the user`s email.
     * @throws NotAuthorisedUserException if email of user is not activated.
     */

    String getNoAuthenticatedUserEmail() throws NotAuthorisedUserException;

    /**
     * Gets authorised user.
     *
     * @return authorised user.
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
     * Activates new account.
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
     * Initializes a new user.
     *
     * @param account            user`s account.
     * @param email              user`s email.
     * @param accountStatus      user`s status.
     * @param deactivated        is true after simple registration and false after registration by facebook or google.
     * @param authenticationType type of registration (LOCAL, GOOGLE, FACEBOOK).
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
