package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.userProfileDTO.PersonalInfoDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.userProfileDTO.ProfileDataDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.userProfileDTO.ProfileImageDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonImageDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonPasswordDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonPersonalInfoDTO;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.UserProfileService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ProfileDataDTO getProfileData() throws NotAuthorisedUserException {
        User user = userProfileService.getProfileData();
        Link self = linkTo(methodOn(UserProfileController.class).getProfileData()).withSelfRel();
        return DTOBuilder.buildDtoForEntity(user, ProfileDataDTO.class, self);
    }

    @Auditable(action = AuditingAction.EDIT_PERSONAL_DATA)
    @PutMapping(value = "/personal-info")
    @ResponseStatus(HttpStatus.OK)
    public PersonalInfoDTO updatePersonalInfo(@Validated(Request.class) @RequestBody JsonPersonalInfoDTO personalInfo)
            throws NotAuthorisedUserException {
        Person person = userProfileService.updatePersonalInfo(personalInfo);
        return DTOBuilder.buildDtoForEntity(person, PersonalInfoDTO.class);
    }

    @Auditable(action = AuditingAction.CHANGE_PASSWORD)
    @PutMapping(value = "/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Validated(Request.class) @RequestBody JsonPasswordDTO passwordDTO)
            throws NotAuthorisedUserException, IllegalArgumentException {
        userProfileService.changePassword(passwordDTO);
    }

    @Auditable(action = AuditingAction.UPLOAD_IMAGE_PROFILE)
    @PostMapping("/image")
    @ResponseStatus(HttpStatus.OK)
    public ProfileImageDTO uploadProfileImage(@RequestBody JsonImageDTO imageDTO)
            throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        Person person = userProfileService.uploadProfileImage(imageDTO);
        return DTOBuilder.buildDtoForEntity(person, ProfileImageDTO.class);
    }

    @Auditable(action = AuditingAction.DELETE_PROFILE_IMAGE)
    @DeleteMapping("/image")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProfileImage() throws NotAuthorisedUserException {
        userProfileService.deleteProfileImage();
    }

    @Auditable(action = AuditingAction.DELETE_PROFILE)
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteProfile() throws NotAuthorisedUserException {
        userProfileService.deleteProfile();
    }
}
