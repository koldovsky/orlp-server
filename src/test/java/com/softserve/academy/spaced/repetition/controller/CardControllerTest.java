package com.softserve.academy.spaced.repetition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.service.CardService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CardControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private CardController cardController;

    @Mock
    private CardService cardService;

    @Mock
    private UserService mockedUserService;

    private static final long RATING = 0L;
    private static final long DECK_ID = 1L;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void testAddCard() throws Exception {
        List<String> imageList = new ArrayList<>();
        imageList.add("image.jpeg");
        Card card = createCard();
        doNothing().when(cardService).addCard(card, 1L, imageList);
        mockMvc.perform(post("/api/decks/{deckId}/cards", 1L)
                .content(new ObjectMapper().writeValueAsString(card))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAreThereNotPostponedCardsAvailable() throws Exception {
        when(cardService.areThereNotPostponedCardsAvailable(1L)).thenReturn(true);
        mockMvc.perform(get("/api/decks/{deckId}/not-postponed", 1L))
                .andExpect(status().isOk());
        verify(cardService, times(1)).areThereNotPostponedCardsAvailable(1L);
    }

    @Test
    public void testUpdateCard() throws Exception {
        List<String> imageList = new ArrayList<>();
        imageList.add("image.jpeg");
        Card card = createCard();
        when(cardService.updateCard(card, 1L, imageList)).thenReturn(createCard());
        mockMvc.perform(put("/api/decks/{deckId}/cards/{cardId}", 1L, 1L)
                .content(new ObjectMapper().writeValueAsString(card))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCard() throws Exception {
        mockMvc.perform(delete("/api/decks/{deckId}/cards/{cardId}", 1L, 1L))
                .andExpect(status().isOk());
        verify(cardService, times(1)).deleteCard(1L);
        verifyNoMoreInteractions(cardService);
    }

    @Test
    public void testGetCardById() throws Exception {
        when(cardService.getCard(eq(1L))).thenReturn(createCard());
        mockMvc.perform(get("/api/decks/{deckId}/cards/{cardId}", 1L, 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"question\": \"How many access modifiers do you know in Java?\"," +
                        "  \"answer\": \"There are 4 access modifiers in Java: public, protected, default and private\"," +
                        "  \"cardId\": 1," +
                        "   \"rating\": 0," +
                        "  \"title\": \"Card 1\"," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/1\"" +
                        "      }]" +
                        "}"));
        verify(cardService, times(1)).getCard(1L);
    }

    @Test
    public void testGetCardsByDeck() throws Exception {
        Long deckId = 1L;
        when(cardService.findAllByDeckId(deckId)).thenReturn(createLearningCards());
        mockMvc.perform(get("/api/decks/{deckId}/cards", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{" +
                        "  \"question\": \"How many access modifiers do you know in Java?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"There are 4 access modifiers in Java: public, protected, default and private\"," +
                        "  \"title\": \"Card 1\"," +
                        "  \"cardId\": 1," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/1\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        " \"question\": \"What are the supported platforms by Java Programming Language?\"," +
                        "  \"rating\": 0," +
                        " \"answer\": \"Java runs on a variety of platforms, such as Windows, Mac OS, and the various" +
                        " versions of UNIX/Linux like HP-Unix, Sun Solaris, Redhat Linux, Ubuntu, CentOS, etc.\"," +
                        "  \"title\": \"Card 2\"," +
                        " \"cardId\": 2," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/2\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"List any five features of Java?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"Some features include Object Oriented, Platform Independent, Robust, " +
                        "Interpreted, Multi-threaded\"," +
                        "  \"title\": \"Card 3\"," +
                        "  \"cardId\": 3," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/3\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"Why is Java Architectural Neutral?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"It’s compiler generates an architecture-neutral object file format, which " +
                        "makes the compiled code to be executable on many processors, with the presence of Java runtime" +
                        " system.\"," +
                        "  \"title\": \"Card 4\"," +
                        "  \"cardId\": 4," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/4\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"How Java enabled High Performance?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"Java uses Just-In-Time compiler to enable high performance. Just-In-Time " +
                        "compiler is a program that turns Java bytecode, which is a program that contains instructions" +
                        " that must be interpreted into instructions that can be sent directly to the processor.\"," +
                        "  \"title\": \"Card 5\"," +
                        "  \"cardId\": 5," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/5\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"Why Java is considered dynamic?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"It is designed to adapt to an evolving environment. Java programs can carry" +
                        " extensive amount of run-time information that can be used to verify and resolve accesses" +
                        " to objects on run-time.\"," +
                        "  \"title\": \"Card 6\"," +
                        "  \"cardId\": 6," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/6\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"What is Java Virtual Machine and how it is considered in context of Java’s" +
                        " platform independent feature?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"When Java is compiled, it is not compiled into platform specific machine," +
                        " rather into platform independent byte code. This byte code is distributed over the web and" +
                        " interpreted by virtual Machine (JVM) on whichever platform it is being run.\"," +
                        "  \"title\": \"Card 7\"," +
                        "  \"cardId\": 7," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/7\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"List two Java IDE’s?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"Netbeans, Eclipse, etc.\"," +
                        "  \"title\": \"Card 8\"," +
                        "  \"cardId\": 8," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/8\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"List some Java keywords(unlike C, C++ keywords)?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"Some Java keywords are import, super, finally, etc.\"," +
                        "  \"title\": \"Card 9\"," +
                        "  \"cardId\": 9," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/9\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"What do you mean by Object?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"Object is a runtime entity and it’s state is stored in fields and behavior" +
                        " is shown via methods. Methods operate on an objects internal state and serve as the" +
                        " primary mechanism for object-to-object communication.\"," +
                        "  \"title\": \"Card 10\"," +
                        "  \"cardId\": 10," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/10\"" +
                        "      }]" +
                        "}" +
                        "]"));
        verify(cardService, times(1)).findAllByDeckId(1L);
    }

    @Test
    public void testGetLearningCards() throws Exception {
        Long deckId = 1L;
        User mockedUser1 = new User(new Account("", "email1@email.com"), new Person("first1", "last1"), new Folder());
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser1);
        when(cardService.getLearningCards(deckId)).thenReturn(createLearningCards());
        mockMvc.perform(get("/api/decks/{deckId}/learn", deckId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{" +
                        "  \"question\": \"How many access modifiers do you know in Java?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"There are 4 access modifiers in Java: public, protected, default and private\"," +
                        "  \"title\": \"Card 1\"," +
                        "  \"cardId\": 1," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/1\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        " \"question\": \"What are the supported platforms by Java Programming Language?\"," +
                        "  \"rating\": 0," +
                        " \"answer\": \"Java runs on a variety of platforms, such as Windows, Mac OS, and the various" +
                        " versions of UNIX/Linux like HP-Unix, Sun Solaris, Redhat Linux, Ubuntu, CentOS, etc.\"," +
                        "  \"title\": \"Card 2\"," +
                        " \"cardId\": 2," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/2\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"List any five features of Java?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"Some features include Object Oriented, Platform Independent, Robust, " +
                        "Interpreted, Multi-threaded\"," +
                        "  \"title\": \"Card 3\"," +
                        "  \"cardId\": 3," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/3\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"Why is Java Architectural Neutral?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"It’s compiler generates an architecture-neutral object file format, which " +
                        "makes the compiled code to be executable on many processors, with the presence of Java runtime" +
                        " system.\"," +
                        "  \"title\": \"Card 4\"," +
                        "  \"cardId\": 4," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/4\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"How Java enabled High Performance?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"Java uses Just-In-Time compiler to enable high performance. Just-In-Time " +
                        "compiler is a program that turns Java bytecode, which is a program that contains instructions" +
                        " that must be interpreted into instructions that can be sent directly to the processor.\"," +
                        "  \"title\": \"Card 5\"," +
                        "  \"cardId\": 5," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/5\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"Why Java is considered dynamic?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"It is designed to adapt to an evolving environment. Java programs can carry" +
                        " extensive amount of run-time information that can be used to verify and resolve accesses" +
                        " to objects on run-time.\"," +
                        "  \"title\": \"Card 6\"," +
                        "  \"cardId\": 6," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/6\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"What is Java Virtual Machine and how it is considered in context of Java’s" +
                        " platform independent feature?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"When Java is compiled, it is not compiled into platform specific machine," +
                        " rather into platform independent byte code. This byte code is distributed over the web and" +
                        " interpreted by virtual Machine (JVM) on whichever platform it is being run.\"," +
                        "  \"title\": \"Card 7\"," +
                        "  \"cardId\": 7," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/7\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"List two Java IDE’s?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"Netbeans, Eclipse, etc.\"," +
                        "  \"title\": \"Card 8\"," +
                        "  \"cardId\": 8," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/8\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"List some Java keywords(unlike C, C++ keywords)?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"Some Java keywords are import, super, finally, etc.\"," +
                        "  \"title\": \"Card 9\"," +
                        "  \"cardId\": 9," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/9\"" +
                        "      }]" +
                        "}," +
                        "{" +
                        "  \"question\": \"What do you mean by Object?\"," +
                        "   \"rating\": 0," +
                        "  \"answer\": \"Object is a runtime entity and it’s state is stored in fields and behavior" +
                        " is shown via methods. Methods operate on an objects internal state and serve as the" +
                        " primary mechanism for object-to-object communication.\"," +
                        "  \"title\": \"Card 10\"," +
                        "  \"cardId\": 10," +
                        "   \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/decks/1/cards/10\"" +
                        "      }]" +
                        "}" +
                        "]"));
        verify(cardService, times(1)).getLearningCards(deckId);
    }

    @Test
    public void testGetAdditionalLearningCards() throws Exception {
        Long deckId = 2L;
        User mockedUser1 = new User(new Account("", "email1@email.com"), new Person("first1", "last1"), new Folder());
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser1);
        when(cardService.getAdditionalLearningCards(deckId)).thenReturn(createAdditionalLearningCards());
        mockMvc.perform(get("/api/decks/{deckId}/learn/additional", deckId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[" +
                                "{" +
                                "  \"question\": \"How many access modifiers do you know in Java?\"," +
                                "   \"rating\": 0," +
                                "  \"answer\": \"There are 4 access modifiers in Java: public, protected, default and private\"," +
                                "  \"title\": \"Card 1\"," +
                                "  \"cardId\": 1," +
                                "   \"links\": [{" +
                                "        \"rel\":\"self\"," +
                                "          \"href\":\"http://localhost/api/decks/2/cards/1\"" +
                                "      }]" +
                                "}," +
                                "{" +
                                "  \"question\": \"List any five features of Java?\"," +
                                "   \"rating\": 0," +
                                "  \"answer\": \"Some features include Object Oriented, Platform Independent, Robust, " +
                                "Interpreted, Multi-threaded\"," +
                                "  \"title\": \"Card 3\"," +
                                "  \"cardId\": 3," +
                                "   \"links\": [{" +
                                "        \"rel\":\"self\"," +
                                "          \"href\":\"http://localhost/api/decks/2/cards/3\"" +
                                "      }]" +
                                "}," +
                                "{" +
                                "  \"question\": \"Why is Java Architectural Neutral?\"," +
                                "   \"rating\": 0," +
                                "  \"answer\": \"It’s compiler generates an architecture-neutral object file format, which " +
                                "makes the compiled code to be executable on many processors, with the presence of Java runtime" +
                                " system.\"," +
                                "  \"title\": \"Card 4\"," +
                                "  \"cardId\": 4," +
                                "   \"links\": [{" +
                                "        \"rel\":\"self\"," +
                                "          \"href\":\"http://localhost/api/decks/2/cards/4\"" +
                                "      }]" +
                                "}," +
                                "{" +
                                "  \"question\": \"Why Java is considered dynamic?\"," +
                                "   \"rating\": 0," +
                                "  \"answer\": \"It is designed to adapt to an evolving environment. Java programs can carry" +
                                " extensive amount of run-time information that can be used to verify and resolve accesses" +
                                " to objects on run-time.\"," +
                                "  \"title\": \"Card 6\"," +
                                "  \"cardId\": 6," +
                                "   \"links\": [{" +
                                "        \"rel\":\"self\"," +
                                "          \"href\":\"http://localhost/api/decks/2/cards/6\"" +
                                "      }]" +
                                "}" +
                                "]"));
        verify(cardService, times(1)).getAdditionalLearningCards(deckId);
    }

    private Card createCard(Long id, String title, String question, String answer, Deck deck) {
        Card card = new Card();
        card.setId(id);
        card.setTitle(title);
        card.setQuestion(question);
        card.setAnswer(answer);
        card.setRating(RATING);
        card.setDeck(deck);
        return card;
    }

    private Card createCard() {
        Deck deck = new Deck();
        deck.setId(DECK_ID);
        Card card = new Card();
        card.setId(1L);
        card.setTitle("Card 1");
        card.setAnswer("There are 4 access modifiers in Java: public, protected, default and private");
        card.setQuestion("How many access modifiers do you know in Java?");
        card.setDeck(deck);
        card.setRating(RATING);
        return card;
    }

    private List<Card> createAdditionalLearningCards() {
        List<Card> cards = new ArrayList<>();
        Deck deck = new Deck();
        deck.setId(DECK_ID);

        Card card = createCard(1L, "Card 1", "How many access modifiers do you know in Java?",
                "There are 4 access modifiers in Java: public, protected, default and private", deck);

        Card card3 = createCard(3L, "Card 3", "List any five features of Java?", "Some " +
                "features include Object Oriented, Platform Independent, Robust, Interpreted, Multi-threaded", deck);

        Card card4 = createCard(4L, "Card 4", "Why is Java Architectural Neutral?",
                "It’s compiler generates an architecture-neutral object file format, which makes the" +
                        " compiled code to be executable on many processors, with the presence of Java runtime system.",
                deck);

        Card card6 = createCard(6L, "Card 6", "Why Java is considered dynamic?", "It is " +
                "designed to adapt to an evolving environment. Java programs can carry extensive amount of run-time" +
                " information that can be used to verify and resolve accesses to objects on run-time.", deck);

        cards.add(card);
        cards.add(card3);
        cards.add(card4);
        cards.add(card6);

        return cards;
    }

    private List<Card> createLearningCards() {
        List<Card> cards = new ArrayList<>();
        Deck deck = new Deck();
        deck.setId(DECK_ID);

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

        Card card5 = createCard(5L, "Card 5", "How Java enabled High Performance?", "Java uses Just-In-Time compiler" +
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
}

