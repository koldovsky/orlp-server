package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.enums.AuthorityName;
import com.softserve.academy.spaced.repetition.repository.*;
import com.softserve.academy.spaced.repetition.service.AccessToUrlService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("accessToUrlService")
public class AccessToUrlServiceImpl implements AccessToUrlService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private DeckRepository deckRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private DeckCommentRepository deckCommentRepository;
    @Autowired
    private CourseCommentRepository courseCommentRepository;

    @Override
    public boolean hasAccessToCategory(Long categoryId) {
        return categoryRepository.hasAccessToCategory(categoryId).size() > 0;
    }

    @Override
    public boolean hasAccessToCourse(Long categoryId, Long courseId) {
        return courseRepository.getAccessToCourse(categoryId, courseId).size() > 0;
    }

    @Override
    public boolean hasAccessToDeckFromFolder(Long folderId, Long deckId) {
        return folderRepository.getAccessToDeckFromFolder(folderId, deckId).size() > 0;
    }

    @Override
    public boolean hasAccessToCourse(Long categoryId) {
        return courseRepository.getAccessToCourse(categoryId).size() > 0;
    }

    @Override
    public boolean hasAccessToFolder(Long folderId) throws NotAuthorisedUserException {
        Long authorizedUserFolderId = userService.getAuthorizedUser().getFolder().getId();

        return Objects.equals(authorizedUserFolderId, folderId);
    }

    @Override
    public boolean hasAccessToDeleteCommentForCourse(Long commentId) throws NotAuthorisedUserException {
        boolean hasRoleAdmin = userService.getAuthorizedUser().getAccount().getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equals(AuthorityName.ROLE_ADMIN));
        return hasAccessToUpdateCommentForCourse(commentId) || hasRoleAdmin;
    }

    @Override
    public boolean hasAccessToDeleteCommentForDeck(Long commentId) throws NotAuthorisedUserException {
        boolean hasRoleAdmin = userService.getAuthorizedUser().getAccount().getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equals(AuthorityName.ROLE_ADMIN));
        return hasAccessToUpdateCommentForDeck(commentId) || hasRoleAdmin;
    }

    @Override
    public boolean hasAccessToUpdateCommentForDeck(Long commentId) throws NotAuthorisedUserException {
        Person authorizedPerson = userService.getAuthorizedUser().getPerson();
        Person createdCommentPerson = deckCommentRepository.findOne(commentId).getPerson();
        return authorizedPerson.equals(createdCommentPerson);
    }

    @Override
    public boolean hasAccessToUpdateCommentForCourse(Long commentId) throws NotAuthorisedUserException {
        Person authorizedPerson = userService.getAuthorizedUser().getPerson();
        Person createdCommentPerson = courseCommentRepository.findOne(commentId).getPerson();
        return authorizedPerson.equals(createdCommentPerson);
    }
}
