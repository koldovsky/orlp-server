package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.service.CardService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    public void getCardById() throws Exception {
        when(cardService.getCard(eq(1L))).thenReturn(DomainFactory.createCard(RATING,DECK_ID));
        mockMvc.perform(get("/api/card/{cardId}", 1L)
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
                        "          \"href\":\"http://localhost/api/card/1\"" +
                        "      }]" +
                        "}"));
    }

    @Test
    public void getLearningCards() throws Exception {
        Long deckId = 1L;
        User mockedUser1 = new User(new Account("","email1@email.com"), new Person("first1", "last1"), new Folder());
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser1);
        when(cardService.getLearningCards(deckId)).thenReturn(DomainFactory.createLearningCards(DECK_ID));
        mockMvc.perform(get("/api/decks/{deckId}/learn", 1L)
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
    }


}

