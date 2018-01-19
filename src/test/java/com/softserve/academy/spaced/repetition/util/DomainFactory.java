package com.softserve.academy.spaced.repetition.util;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.ImageType;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class DomainFactory {

    public static User createUser(Long userId, Account account, Person person, Folder folder, Set<Course> courses) {
        User user = new User();
        user.setId(userId);
        user.setAccount(account);
        user.setPerson(person);
        user.setFolder(folder);
        user.setCourses(courses);
        return user;
    }

    public static Account createAccount(Long accountId, String password, String email, AuthenticationType authenticationType,
                                        AccountStatus status, boolean deactivated, Date lastPasswordResetDate,
                                        Set<Authority> authorities, LearningRegime learningRegime, Integer cardsNumber,
                                        List<RememberingLevel> rememberingLevels) {
        Account account = new Account();
        account.setId(accountId);
        account.setPassword(password);
        account.setEmail(email);
        account.setAuthenticationType(authenticationType);
        account.setStatus(status);
        account.setDeactivated(deactivated);
        account.setLastPasswordResetDate(lastPasswordResetDate);
        account.setAuthorities(authorities);
        account.setLearningRegime(learningRegime);
        account.setCardsNumber(cardsNumber);
        account.setRememberingLevels(rememberingLevels);
        return account;
    }

    public static Person createPerson(Long personId, String firstName, String lastName, ImageType imageType,
                                      String image, String imageBase64) {
        Person person = new Person();
        person.setId(personId);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setTypeImage(imageType);
        person.setImage(image);
        person.setImageBase64(imageBase64);
        return person;
    }

    public static Category createCategory(Long categoryId, String name, String description, Image image,
                                          List<Course> courses, List<Deck> decks) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName(name);
        category.setDescription(description);
        category.setImage(image);
        category.setCourses(courses);
        category.setDecks(decks);
        return category;
    }

    public static Course createCourse(Long courseId, String name, String description, Image image, double rating,
                                      boolean published, User owner, Category category, List<Deck> decks,
                                      List<CourseRating> courseRatings, List<CourseComment> courseComments) {
        Course course = new Course();
        course.setId(courseId);
        course.setName(name);
        course.setDescription(description);
        course.setImage(image);
        course.setRating(rating);
        course.setPublished(published);
        course.setOwner(owner);
        course.setCategory(category);
        course.setDecks(decks);
        course.setCourseRatings(courseRatings);
        course.setCourseComments(courseComments);
        return course;
    }

    public static Folder createFolder(Long folderId, Set<Deck> decks) {
        Folder folder = new Folder();
        folder.setId(folderId);
        folder.setDecks(decks);
        return folder;
    }

    public static Deck createDeck(Long deckId, String name, String description, String syntaxToHighLight,
                                  Category category, double rating, User deckOwner, List<Card> cards,
                                  List<Course> courses, List<DeckRating> deckRatings, Set<Folder> folders,
                                  List<DeckComment> deckComments) {
        Deck deck = new Deck();
        deck.setId(deckId);
        deck.setName(name);
        deck.setDescription(description);
        deck.setSynthaxToHighlight(syntaxToHighLight);
        deck.setCategory(category);
        deck.setRating(rating);
        deck.setDeckOwner(deckOwner);
        deck.setCards(cards);
        deck.setCourses(courses);
        deck.setDeckRatings(deckRatings);
        deck.setFolders(folders);
        deck.setDeckComments(deckComments);
        return deck;
    }

    public static DeckComment createDeckComment(Long deckCommentId, String commentText, Date commentDate, Person person,
                                                Long parentCommentId, Deck deck) {
        DeckComment deckComment = new DeckComment();
        deckComment.setId(deckCommentId);
        deckComment.setCommentText(commentText);
        deckComment.setCommentDate(commentDate);
        deckComment.setPerson(person);
        deckComment.setParentCommentId(parentCommentId);
        deckComment.setDeck(deck);
        return  deckComment;
    }

    public static DeckRating createDeckRating(Long deckRatingId, String accountEmail, Deck deck, int rating) {
        DeckRating deckRating = new DeckRating();
        deckRating.setId(deckRatingId);
        deckRating.setAccountEmail(accountEmail);
        deckRating.setDeck(deck);
        deckRating.setRating(rating);
        return deckRating;
    }
}
