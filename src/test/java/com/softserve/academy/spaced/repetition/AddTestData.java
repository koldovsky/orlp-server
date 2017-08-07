package com.softserve.academy.spaced.repetition;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddTestData{

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void addCategories() throws IOException {
        if (categoryRepository.findAll().isEmpty()) {
            List<Category> categories = new LinkedList<>();
            Resource categoryResource = new ClassPathResource("/Data/Category.txt");
            FileReader categoryFileReader = new FileReader(categoryResource.getFile());
            BufferedReader categoryReader = new BufferedReader(categoryFileReader);
            Stream<String> categoryStream = categoryReader.lines();
            categoryStream.map(line -> {
                String[] s = line.split("~");
                return new Category(s[0].trim(), s[1], s[2]);
            }).forEach(categories::add);
            categoryRepository.save(categories);
        } else {
            fail("Category already present");
        }
    }

    @Test
    public void addUsers() throws IOException {
        if (userRepository.findAll().isEmpty()) {
            List<User> users = new LinkedList<>();
            Resource userResource = new ClassPathResource("/Data/Users.txt");
            FileReader userFileReader = new FileReader(userResource.getFile());
            BufferedReader userReader = new BufferedReader(userFileReader);
            Set<Authority> authorities = new HashSet<>();
            authorities.add(new Authority(AuthorityName.ROLE_USER));
            Stream<String> userStream = userReader.lines();
            List<String> userList = userStream.collect(Collectors.toList());
            for (String l : userList) {
                String[] s = l.split("~");
                User user = new User(new Account(bCryptPasswordEncoder.encode(s[0]), s[1], new Date(), authorities, AccountStatus.ACTIVE), new Person(s[2], s[3]), new Folder());
                users.add(user);
            }
            userRepository.save(users);
        } else {
            fail("User already present");
        }
    }
}