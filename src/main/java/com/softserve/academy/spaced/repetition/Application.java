package com.softserve.academy.spaced.repetition;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jarki on 6/26/2017.
 */

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
//            String[] s = line.split("#");
//            Category category = new Category(s[0].trim(), s[1]);
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
//
//        userStream.map(line -> {
//            String[] s = line.split("~");
//            User user = new User(new Account(s[0], s[1]), new Person(s[2], s[3]), new Folder());
//            return user;
//        }).forEach(users::add);
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
//        int m = 8;
//        for (String l: deckList) {
//            String s[] = l.split("~");
//            Deck deck = new Deck(s[0], s[1], categories.get(i), users.get(o), cards.subList(n, m));
//            if (i < 9) {
//                i++;
//            }else i = 0;
//            o++;
//            n = m;
//            m += 8;
//            decks.add(deck);
//        }
//
//        deckRepository.save(decks);
//        decks = deckRepository.findAll();
//
//        List<Course> courses = new ArrayList<>();
//        Resource coursesResource = new ClassPathResource("/Data/deck.txt");
//        BufferedReader coursesReader = new BufferedReader(new FileReader(coursesResource.getFile()));
//        Stream<String> coursesStream = coursesReader.lines();
//        List<String> coursesList = coursesStream.collect(Collectors.toList());
//        i = 0;
//        n = 0;
//        m = 1;
//        for (String l: coursesList) {
//            String s[] = l.split("~");
//            Course course = new Course(s[0], s[1], categories.get(i), decks.subList(n, m));
//            courses.add(course);
//            if (i < 9) {
//                i++;
//            }else i = 0;
//            n = m;
//            m++;
//
//        }
//
//        courseRepository.save(courses);


  //  }
}
