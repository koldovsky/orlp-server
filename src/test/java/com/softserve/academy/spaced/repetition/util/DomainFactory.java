package com.softserve.academy.spaced.repetition.util;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.ImageType;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;

import java.util.*;

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

    public static Card createCard(long rating, long deckId) {
        Deck deck = new Deck();
        deck.setId(deckId);
        Card card = new Card();
        card.setId(1L);
        card.setTitle("Card 1");
        card.setAnswer("There are 4 access modifiers in Java: public, protected, default and private");
        card.setQuestion("How many access modifiers do you know in Java?");
        card.setDeck(deck);
        card.setRating(rating);
        return card;
    }

    public static List<Card> createLearningCards(long deckId) {
        List<Card> cards = new ArrayList<>();
        Deck deck = new Deck();
        deck.setId(deckId);
        Card card = createCard(1L, "Card 1", "How many access modifiers do you know in Java?",
                "There are 4 access modifiers in Java: public, protected, default and private", deck);
        Card card2 = createCard(2L, "Card 2", "What are the supported platforms by Java Programming" +
                " Language?", "Java runs on a variety of platforms, such as Windows, Mac OS, and the various" +
                " versions of UNIX/Linux like HP-Unix, Sun Solaris, Redhat Linux, Ubuntu, CentOS, etc.", deck);
        Card card3 = createCard(3L, "Card 3", "List any five features of Java?", "Some " +
                "features include Object Oriented, Platform Independent, Robust, Interpreted, Multi-threaded", deck);
        Card card4 = createCard(4L, "Card 4", "Why is Java Architectural Neutral?",
                "It’s compiler generates an architecture-neutral object file format, which makes the" +
                        " compiled code to be executable on many processors, with the presence of Java runtime system.",
                deck);
        Card card5 = createCard(5L, "Card 5", "How Java enabled High Performance?", "Java " +
                "uses Just-In-Time compiler" +
                " to enable high performance. Just-In-Time compiler is a program that turns Java bytecode, which is" +
                " a program that contains instructions that must be interpreted into instructions that can be sent" +
                " directly to the processor.", deck);
        Card card6 = createCard(6L, "Card 6", "Why Java is considered dynamic?", "It is " +
                "designed to adapt to an evolving environment. Java programs can carry extensive amount of run-time" +
                " information that can be used to verify and resolve accesses to objects on run-time.", deck);
        Card card7 = createCard(7L, "Card 7", "What is Java Virtual Machine and how it is considered" +
                " in context of Java’s platform independent feature?", "When Java is compiled, it is not" +
                " compiled into platform specific machine, rather into platform independent byte code. This byte code" +
                " is distributed over the web and interpreted by virtual Machine (JVM) on whichever platform it is " +
                "being run.", deck);
        Card card8 = createCard(8L, "Card 8", "List two Java IDE’s?", "Netbeans, Eclipse, etc."
                , deck);
        Card card9 = createCard(9L, "Card 9", "List some Java keywords(unlike C, C++ keywords)?",
                "Some Java keywords are import, super, finally, etc.", deck);
        Card card10 = createCard(10L, "Card 10", "What do you mean by Object?", "Object is a" +
                " runtime entity and it’s state is stored in fields and behavior is shown via methods. Methods" +
                " operate on an objects internal state and serve as the primary mechanism for object-to-object" +
                " communication.", deck);
        cards.add(card);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);
        cards.add(card6);
        cards.add(card7);
        cards.add(card8);
        cards.add(card9);
        cards.add(card10);
        return cards;
    }

    public static Category createCategory(Long id, String name, String description, Image image) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        category.setImage(image);
        return category;
    }

    public static List<Category> createListOfCategory(Long category_id) {
        List<Category> categoryList = new ArrayList<>();
        Category category1 = createCategory(category_id, "Category1", "description",
                null, null, null);
        categoryList.add(category1);
        return categoryList;
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

    public static CardRating createCardRating(Long id, String accountEmail, Card card, int rating) {
        CardRating cardRating = new CardRating();
        cardRating.setId(id);
        cardRating.setAccountEmail(accountEmail);
        cardRating.setCard(card);
        cardRating.setRating(rating);
        return cardRating;
    }

    public static Image createImage(Long id, String imagebase64, String type, User createdBy, Long size, boolean isImageUsed){
        Image image = new Image();
        image.setId(id);
        image.setImagebase64(imagebase64);
        image.setType(type);
        image.setCreatedBy(createdBy);
        image.setSize(size);
        image.setIsImageUsed(isImageUsed);
        return image;
    }

    public static CourseRating createCourseRating(Long id, String accountEmail, Course course, int rating){
        CourseRating courseRating = new CourseRating();
        courseRating.setId(id);
        courseRating.setAccountEmail(accountEmail);
        courseRating.setCourse(course);
        courseRating.setRating(rating);
        return courseRating;
    }

    public static List<Course> createCourseList(Course course) {
        List<Course> courseList = new ArrayList<>();
        courseList.add(course);
        return courseList;
    }

    public static Set<Course> createCourseSet(Course course) {
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        return courseSet;
    }

    public static List<Deck> createDeckList(Deck deck) {
        List<Deck> deckList = new ArrayList<>();
        deckList.add(deck);
        return deckList;
    }

    public static Set<Deck> createDeckSet(Deck deck) {
        Set<Deck> deckSet = new HashSet<>();
        deckSet.add(deck);
        return deckSet;
    }

    public static List<Card> createCardList(Card card) {
        List<Card> cardList = new ArrayList<>();
        cardList.add(card);
        return cardList;
    }
}
