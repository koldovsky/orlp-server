package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.AuthorityName;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.*;
import com.softserve.academy.spaced.repetition.service.AccessToUrlService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    public boolean hasAccessToCategory(Long category_id) {
        return categoryRepository.hasAccessToCategory(category_id).size() > 0;
    }

    @Override
    public boolean hasAccessToCourse(Long category_id, Long course_id) {
        return courseRepository.getAccessToCourse(category_id, course_id).size() > 0;
    }

    @Override
    public boolean hasAccessToDeckFromFolder(Long folder_id, Long deckId) {
        return folderRepository.getAccessToDeckFromFolder(folder_id, deckId).size() > 0;
    }

    @Override
    public boolean hasAccessToCourse(Long category_id) {
        return courseRepository.getAccessToCourse(category_id).size() > 0;
    }

    @Override
    public boolean hasAccessToDeck(Long category_id, Long course_id, Long deck_id) {
        return hasAccessToCourse(category_id, course_id) & (deckRepository.hasAccessToDeck(course_id, deck_id).size() > 0);
    }

    @Override
    public boolean hasAccessToDeckFromCategory(Long category_id, Long deck_id) {
        return deckRepository.hasAccessToDeckFromCategory(category_id, deck_id).size() > 0;
    }

    @Override
    public boolean hasAccessToDeck(Long category_id) {
        return deckRepository.hasAccessToDeckFromCategory(category_id).size() > 0;
    }

    @Override
    public boolean hasAccessToCard(Long deck_id, Long card_id) {
        return (cardRepository.hasAccessToCard(deck_id, card_id).size() > 0);
    }

    @Override
    public boolean hasAccessToCard(Long category_id, Long deck_id, Long card_id) {
        return hasAccessToDeckFromCategory(category_id, deck_id) & (cardRepository.hasAccessToCard(deck_id, card_id).size() > 0);
    }

    @Override
    public boolean hasAccessToCard(Long category_id, Long course_id, Long deck_id, Long card_id) {
        return hasAccessToDeck(category_id, course_id, deck_id) & (cardRepository.hasAccessToCard(deck_id, card_id).size() > 0);
    }

    @Override
    public boolean hasAccessToFolder(Long folder_id) throws NotAuthorisedUserException {
        Long authorizedUserFolderId = userService.getAuthorizedUser().getFolder().getId();

        return Objects.equals(authorizedUserFolderId, folder_id);
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
