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

//    @Autowired
//    CategoryRepository categoryRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    DeckRepository deckRepository;
//    @Autowired
//    CourseRepository courseRepository;
//    @Autowired
//    CardRepository cardRepository;
//
//    @Autowired
//    PasswordEncoder bCryptPasswordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

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
//        int i = 0;
//        int o = 0;
//        int n = 0;
//        int m = 10;
//        for (String l : deckList) {
//            String s[] = l.split("~");
//            // Insert into Deck table
//            Deck deck = new Deck();
//            deck.setName(s[0]);
//            deck.setDescription(s[1]);
//            deck.setCategory(categories.get(i));
//            deck.setDeckOwner(users.get(o));
//            deck.setCards(cards.subList(n, m));
//            if (i < 9) {
//                i++;
//            } else i = 0;
//            o++;
//            n = m;
//            m += 10;
//            decks.add(deck);
//        }
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
//            courses.add(course);
//            if (i < 9) {
//                i++;
//            } else i = 0;
//            n = m;
//            m += 2;
//        }
//        courses.get(1).setCategory(categories.get(0)); // Changed Android to Java category
//        courseRepository.save(courses);
//
//        n = 0;
//        m = 2;
//        for (Category category : categories) {
//            category.setDecks(decks.subList(n, m));
//            n = m;
//            m += 2;
//            categoryRepository.save(category);
//        }
//    }
}
