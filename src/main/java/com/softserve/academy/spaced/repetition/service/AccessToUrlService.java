package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.AuthorityName;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component("accessToUrlService")
public class AccessToUrlService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final FolderRepository folderRepository;
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final DeckCommentRepository deckCommentRepository;
    private final CourseCommentRepository courseCommentRepository;

    @Autowired
    public AccessToUrlService(CategoryRepository categoryRepository, UserService userService, CourseRepository courseRepository, FolderRepository folderRepository, DeckRepository deckRepository, CardRepository cardRepository, DeckCommentRepository deckCommentRepository, CourseCommentRepository courseCommentRepository) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.courseRepository = courseRepository;
        this.folderRepository = folderRepository;
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        this.deckCommentRepository = deckCommentRepository;
        this.courseCommentRepository = courseCommentRepository;
    }

    public boolean hasAccessToCategory(Long category_id) {
        return categoryRepository.hasAccessToCategory(category_id).size() > 0;
    }

    public boolean hasAccessToCourse(Long category_id, Long course_id) {
        return courseRepository.getAccessToCourse(category_id, course_id).size() > 0;
    }

    public boolean hasAccessToDeckFromFolder(Long folder_id, Long deckId) {
        return folderRepository.getAccessToDeckFromFolder(folder_id, deckId).size() > 0;
    }

    public boolean hasAccessToCourse(Long category_id) {
        return courseRepository.getAccessToCourse(category_id).size() > 0;
    }

    public boolean hasAccessToDeck(Long category_id, Long course_id, Long deck_id) {
        return hasAccessToCourse(category_id, course_id) & (deckRepository.hasAccessToDeck(course_id, deck_id).size() > 0);
    }

    public boolean hasAccessToDeckFromCategory(Long category_id, Long deck_id) {
        return deckRepository.hasAccessToDeckFromCategory(category_id, deck_id).size() > 0;
    }

    public boolean hasAccessToDeck(Long category_id) {
        return deckRepository.hasAccessToDeckFromCategory(category_id).size() > 0;
    }

    public boolean hasAccessToCard(Long deck_id, Long card_id) {
        return (cardRepository.hasAccessToCard(deck_id, card_id).size() > 0);
    }

    public boolean hasAccessToCard(Long category_id, Long deck_id, Long card_id) {
        return hasAccessToDeckFromCategory(category_id, deck_id) & (cardRepository.hasAccessToCard(deck_id, card_id).size() > 0);
    }

    public boolean hasAccessToCard(Long category_id, Long course_id, Long deck_id, Long card_id) {
        return hasAccessToDeck(category_id, course_id, deck_id) & (cardRepository.hasAccessToCard(deck_id, card_id).size() > 0);
    }

    public boolean hasAccessToFolder(Long folder_id) throws NotAuthorisedUserException {
        Long authorizedUserFolderId = userService.getAuthorizedUser().getFolder().getId();

        return Objects.equals(authorizedUserFolderId, folder_id);
    }

    public boolean hasAccessToDeleteCommentForCourse(Long commentId) throws NotAuthorisedUserException {
        boolean hasRoleAdmin = userService.getAuthorizedUser().getAccount().getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equals(AuthorityName.ROLE_ADMIN));
        return hasAccessToUpdateCommentForCourse(commentId) || hasRoleAdmin;
    }

    public boolean hasAccessToDeleteCommentForDeck(Long commentId) throws NotAuthorisedUserException {
        boolean hasRoleAdmin = userService.getAuthorizedUser().getAccount().getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equals(AuthorityName.ROLE_ADMIN));
        return hasAccessToUpdateCommentForDeck(commentId) || hasRoleAdmin;
    }

    public boolean hasAccessToUpdateCommentForDeck(Long commentId) throws NotAuthorisedUserException {
        Person authorizedPersonId = userService.getAuthorizedUser().getPerson();
        Person createdCommentPersonId = deckCommentRepository.findOne(commentId).getPerson();
        return authorizedPersonId.equals(createdCommentPersonId);
    }

    public boolean hasAccessToUpdateCommentForCourse(Long commentId) throws NotAuthorisedUserException {
        Person authorizedPersonId = userService.getAuthorizedUser().getPerson();
        Person createdCommentPersonId = courseCommentRepository.findOne(commentId).getPerson();
        return authorizedPersonId.equals(createdCommentPersonId);
    }


}
