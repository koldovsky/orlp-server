package com.softserve.academy.spaced.repetition;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
@SpringBootApplication
public class Application {

//    private final CategoryRepository categoryRepository;
//    private final UserRepository userRepository;
//    private final DeckRepository deckRepository;
//    private final CourseRepository courseRepository;
//    private final CardRepository cardRepository;
//    private final PasswordEncoder bCryptPasswordEncoder;
//
//    @Autowired
//    public Application(CategoryRepository categoryRepository, UserRepository userRepository, DeckRepository deckRepository, CourseRepository courseRepository, CardRepository cardRepository, PasswordEncoder bCryptPasswordEncoder) {
//        this.categoryRepository = categoryRepository;
//        this.userRepository = userRepository;
//        this.deckRepository = deckRepository;
//        this.courseRepository = courseRepository;
//        this.cardRepository = cardRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @PostConstruct
//    public void addTestData() throws IOException {
//        List<Card> cards = new LinkedList<>();
//
//        Card card1 = new Card("What are the classes implementing List and Set interface ?", "Class implementing List interface :  ArrayList , Vector , LinkedList ,\n" +
//                "\n" +
//                "Class implementing Set interface :  HashSet , TreeSet");
//        Card card2 = new Card("How to create ArrayList in Java?", "List<String> list = new ArrayList<>()");
//        Card card3 = new Card("You have hash set students. How to add some data to it.", "students.add(\"Student1\");");
//        Card card4 = new Card("How to delete item from LinkedList?", "list.remove(item);");
//
//        cards.add(card1);
//        cards.add(card2);
//        cards.add(card3);
//        cards.add(card4);
//
//        testServiceToAddCards.addDeck(cards);
//    }

//    @PostConstruct
//    public void addTestData() throws IOException {
//        List<Category> categories = new ArrayList<>();
//        Resource categoryResource = new ClassPathResource("/Data/Category.txt");
//        BufferedReader categoryReader = new BufferedReader(new FileReader(categoryResource.getFile()));
//        Stream<String> categoryStream = categoryReader.lines();
//
//        categoryStream.map(line -> {
//            String[] s = line.split("~");
//
//            Category category = new Category(s[0].trim(), s[1], s[2]);
//            return category;
//        }).forEach(categories::add);
//
//        categoryRepository.save(categories);
//        categories = categoryRepository.findAll();
//
//        List<User> users = new ArrayList<>();
//        Resource userResource = new ClassPathResource("/Data/Users.txt");
//        BufferedReader userReader = new BufferedReader(new FileReader(userResource.getFile()));
//        Stream<String> userStream = userReader.lines();
//        Set<Authority> authorities = new HashSet<>();
//        authorities.add(new Authority(AuthorityName.ROLE_USER));
//        AccountStatus status = AccountStatus.ACTIVE;
//        List<String> userList = userStream.collect(Collectors.toList());
//
//        for (String l : userList) {
//            String[] s = l.split("~");
//            User user = new User(new Account(bCryptPasswordEncoder.encode(s[0]), s[1], new Date(), authorities, status), new Person(s[2], s[3]), new Folder());
//            users.add(user);
//        }
//
//        userRepository.save(users);
//        users = userRepository.findAll();
//
//        List<Card> cards = new ArrayList<>();
//        Resource cardResource = new ClassPathResource("/Data/cards.txt");
//        BufferedReader cardReader = new BufferedReader(new FileReader(cardResource.getFile()));
//        Stream<String> cardStream = cardReader.lines();
//
//        cardStream.map(line -> {
//            String[] s = line.split("~");
//            Card card = new Card(s[0], s[1]);
//            return card;
//        }).forEach(cards::add);
//
//        cardRepository.save(cards);
//        cards = cardRepository.findAll();
//
//        List<Deck> decks = new ArrayList<>();
//        Resource deckResource = new ClassPathResource("/Data/deck.txt");
//        BufferedReader deckReader = new BufferedReader(new FileReader(deckResource.getFile()));
//        Stream<String> deckStream = deckReader.lines();
//        List<String> deckList = deckStream.collect(Collectors.toList());
//
//            int i = 0;
//            int o = 0;
//            int n = 0;
//            int m = 10;
//            for (String l : deckList) {
//                String s[] = l.split("~");
//                // Insert into Deck table
//                Deck deck = new Deck();
//                deck.setName(s[0]);
//                deck.setDescription(s[1]);
//                deck.setCategory(categories.get(i));
//                deck.setDeckOwner(users.get(o));
//                deck.setCards(cards.subList(n, m));
//                if (i < 9) {
//                    i++;
//                } else i = 0;
//                o++;
//                n = m;
//                m += 10;
//                decks.add(deck);
//            }
//
//        deckRepository.save(decks);
//        decks = deckRepository.findAll();
//
//
//        List<Course> courses = new ArrayList<>();
//        Resource coursesResource = new ClassPathResource("/Data/course.txt");
//        BufferedReader coursesReader = new BufferedReader(new FileReader(coursesResource.getFile()));
//        Stream<String> coursesStream = coursesReader.lines();
//        List<String> coursesList = coursesStream.collect(Collectors.toList());
//        i = 0;
//        n = 0;
//        m = 2;
//        for (String l : coursesList) {
//            String s[] = l.split("~");
//            // Insert into Course table
//            Course course = new Course();
//            course.setName(s[0]);
//            course.setDescription(s[1]);
//            course.setImagebase64(s[2]);
//            course.setCategory(categories.get(i));
//            course.setDecks(decks.subList(n, m));
//            categories.get(i).setDecks(course.getDecks());
//            courses.add(course);
//            if (i < 9) {
//                i++;
//            } else i = 0;
//            n = m;
//            m += 2;
//        }
//        courseRepository.save(courses);
//
//        int a = 0;
//        int b = 1;
//        for (Category category : categories) {
//            List<Deck> decks1 = new ArrayList<>();
//            decks1.addAll(courses.get(a).getDecks());
//            category.setDecks(decks1);
//            category.setCourses(courses.subList(a, b));
//            a = b;
//            b++;
//            categoryRepository.save(category);
//        }
//    }
}

