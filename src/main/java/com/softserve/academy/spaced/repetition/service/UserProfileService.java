package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonImageDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonPasswordDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonPersonalInfoDTO;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

/**
 * This interface works with user profile data
 */
public interface UserProfileService {
    /**
     * Get profile data of current user.
     *
     * @return user that contain profile data.
     * @throws NotAuthorisedUserException if unauthorised user tries to get profile data.
     */
    User getProfileData() throws NotAuthorisedUserException;

    /**
     * Change first and last name of person.
     *
     * @param personalInfo dto that contain personal info.
     * @return person with changed personal data.
     * @throws NotAuthorisedUserException if unauthorised user tries to change personal data.
     */
    Person updatePersonalInfo(JsonPersonalInfoDTO personalInfo) throws NotAuthorisedUserException;

    /**
     * Change password of account.
     *
     * @param passwordDTO dto object that contain current and new password.
     * @return account with changed password.
     * @throws NotAuthorisedUserException if unauthorised user tries to change password.
     * @throws IllegalArgumentException   if current password and account password don't match.
     */
    Account changePassword(JsonPasswordDTO passwordDTO) throws NotAuthorisedUserException, IllegalArgumentException;

    /**
     * Upload image to user profile.
     *
     * @param imageDTO object that contain encoded image in base64.
     * @return person with uploaded image.
     * @throws NotAuthorisedUserException                if unauthorised user tries to upload image.
     * @throws ImageRepositorySizeQuotaExceededException if user quota is exceeded.
     */
    Person uploadProfileImage(JsonImageDTO imageDTO)
            throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException;

    /**
     * Delete profile image of current user.
     *
     * @throws NotAuthorisedUserException if unauthorised user tries to delete image.
     */
    void deleteProfileImage() throws NotAuthorisedUserException;

    /**
     * Delete profile of user.
     *
     * @throws NotAuthorisedUserException if unauthorised tries to delete account.
     */
    void deleteProfile() throws NotAuthorisedUserException;
}
