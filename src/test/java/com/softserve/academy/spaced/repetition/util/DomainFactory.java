package com.softserve.academy.spaced.repetition.util;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.*;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;

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

    public static Authority createAuthority(Long authorityId, AuthorityName name, List<Account> accounts) {
        Authority authority = new Authority();
        authority.setId(authorityId);
        authority.setName(name);
        authority.setAccounts(accounts);
        return authority;
    }

    public static Person createPerson(Long personId, String firstName, String lastName, ImageType imageType,
                                      String image, String imageBase64) {
        Person person = new Person();
        person.setId(personId);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setImageType(imageType);
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

    public static Category createCategory(Long id, String name, String description, Image image) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        category.setImage(image);
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

    public static CourseRating createCourseRating(Long id, String accountEmail, Course course, int rating) {
        CourseRating courseRating = new CourseRating();
        courseRating.setId(id);
        courseRating.setAccountEmail(accountEmail);
        courseRating.setCourse(course);
        courseRating.setRating(rating);
        return courseRating;
    }

    public static Folder createFolder(Long folderId, Set<Deck> decks) {
        Folder folder = new Folder();
        folder.setId(folderId);
        folder.setDecks(decks);
        return folder;
    }

    public static Deck createDeck(Long deckId, String name, String description, String syntaxToHighlight,
                                  Category category, double rating, User deckOwner, List<Card> cards,
                                  List<DeckRating> deckRatings, Set<Folder> folders, List<DeckComment> deckComments) {
        Deck deck = new Deck();
        deck.setId(deckId);
        deck.setName(name);
        deck.setDescription(description);
        deck.setSyntaxToHighlight(syntaxToHighlight);
        deck.setCategory(category);
        deck.setRating(rating);
        deck.setDeckOwner(deckOwner);
        deck.setCards(cards);
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
        return deckComment;
    }

    public static DeckRating createDeckRating(Long deckRatingId, String accountEmail, Deck deck, int rating) {
        DeckRating deckRating = new DeckRating();
        deckRating.setId(deckRatingId);
        deckRating.setAccountEmail(accountEmail);
        deckRating.setDeck(deck);
        deckRating.setRating(rating);
        return deckRating;
    }

    public static Card createCard(Long id, String title, String question, String answer, Deck deck) {
        Card card = new Card();
        card.setId(id);
        card.setTitle(title);
        card.setQuestion(question);
        card.setAnswer(answer);
        card.setRating(0L);
        card.setDeck(deck);
        return card;
    }

    public static CardRating createCardRating(Long id, String accountEmail, Card card, int rating) {
        CardRating cardRating = new CardRating();
        cardRating.setId(id);
        cardRating.setAccountEmail(accountEmail);
        cardRating.setCard(card);
        cardRating.setRating(rating);
        return cardRating;
    }

    public static Audit createAudit(long id, String accountEmail, AuditingAction action, Date time, String ipAddress, String role) {
        Audit audit = new Audit();
        audit.setId(id);
        audit.setAccountEmail(accountEmail);
        audit.setAction(action);
        audit.setTime(time);
        audit.setIpAddress(ipAddress);
        audit.setRole(role);
        return audit;
    }

    public static Image createImage(Long imageId, String imageBase64, String type, User createdBy, Long size,
                                    boolean isImageUsed) {
        Image image = new Image();
        image.setId(imageId);
        image.setImageBase64(imageBase64);
        image.setType(type);
        image.setCreatedBy(createdBy);
        image.setSize(size);
        image.setIsImageUsed(isImageUsed);
        return image;
    }

    public static RememberingLevel createRememberingLevel(Long rememberingLevelId, Integer orderNumber, String name,
                                                          Integer numberOfPostponedDays, Account account) {
        RememberingLevel rememberingLevel = new RememberingLevel();
        rememberingLevel.setId(rememberingLevelId);
        rememberingLevel.setOrderNumber(orderNumber);
        rememberingLevel.setName(name);
        rememberingLevel.setNumberOfPostponedDays(numberOfPostponedDays);
        rememberingLevel.setAccount(account);
        return rememberingLevel;
    }

    public static UserCardQueue createUserCardQueue(Long userCardQueueId, Long userId, Long cardId, Long deckId,
                                                    UserCardQueueStatus status, Date cardDate, Date dateToRepeat,
                                                    RememberingLevel rememberingLevel) {
        UserCardQueue userCardQueue = new UserCardQueue();
        userCardQueue.setId(userCardQueueId);
        userCardQueue.setUserId(userId);
        userCardQueue.setCardId(cardId);
        userCardQueue.setDeckId(deckId);
        userCardQueue.setStatus(status);
        userCardQueue.setCardDate(cardDate);
        userCardQueue.setDateToRepeat(dateToRepeat);
        userCardQueue.setRememberingLevel(rememberingLevel);
        return userCardQueue;
    }

    public static CardImage createCardImage(Long cardImageId, String image, Card card) {
        CardImage cardImage = new CardImage();
        cardImage.setImage(image);
        cardImage.setCard(card);
        cardImage.setId(cardImageId);
        return cardImage;
    }
}
