package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonImageDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonPasswordDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonPersonalInfoDTO;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.enums.ImageType;
import com.softserve.academy.spaced.repetition.service.ImageService;
import com.softserve.academy.spaced.repetition.service.MailService;
import com.softserve.academy.spaced.repetition.service.UserProfileService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final Locale locale = LocaleContextHolder.getLocale();
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MessageSource messageSource;

    @Override
    public User getProfileData() throws NotAuthorisedUserException {
        return userService.getAuthorizedUser();
    }

    @Override
    @Transactional
    public Person updatePersonalInfo(JsonPersonalInfoDTO personalInfo) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Person person = user.getPerson();
        person.setFirstName(personalInfo.getFirstName());
        person.setLastName(personalInfo.getLastName());
        return person;
    }

    @Override
    @Transactional(noRollbackFor = MailSendException.class)
    public Account changePassword(JsonPasswordDTO passwordDTO) throws NotAuthorisedUserException, IllegalArgumentException {
        User user = userService.getAuthorizedUser();
        Account account = user.getAccount();
        String accountPassword = account.getPassword();
        String currentPassword = passwordDTO.getCurrentPassword();
        if (!passwordEncoder.matches(currentPassword, accountPassword)) {
            throw new IllegalArgumentException(messageSource.getMessage("message.exception.passwordNotMatchWithCurrent",
                    new Object[]{}, locale));
        }
        String newPassword = passwordDTO.getNewPassword();
        account.setPassword(passwordEncoder.encode(newPassword));
        mailService.sendPasswordNotificationMail(user);
        return account;
    }

    @Override
    @Transactional
    public Person uploadProfileImage(JsonImageDTO imageDTO) throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        User user = userService.getAuthorizedUser();
        Person person = user.getPerson();
        String imageBase64 = imageDTO.getImageBase64();
        if (imageService.isImageCanBeAddedToProfile(user, imageBase64)) {
            person.setImageBase64(imageBase64);
            person.setImageType(ImageType.BASE64);
        }
        return person;
    }

    @Override
    @Transactional
    public void deleteProfileImage() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Person person = user.getPerson();
        person.setImageBase64(null);
        person.setImageType(ImageType.NONE);
    }

    @Override
    @Transactional
    public void deleteProfile() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Account account = user.getAccount();
        account.setDeactivated(true);
    }
}
