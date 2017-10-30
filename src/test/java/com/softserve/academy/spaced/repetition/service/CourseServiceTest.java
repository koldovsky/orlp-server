package com.softserve.academy.spaced.repetition.service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class CourseServiceTest {
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Mock
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Before
    public void setUp() throws Exception {
        courseService = new CourseService();
        courseService.setCourseRepository(courseRepository);
        courseService.setUserService(userService);
        courseService.setUserRepository(userRepository);
        courseService.setImageService(imageService);
        courseService.setImageRepository(imageRepository);
        courseService.setCategoryRepository(categoryRepository);
        courseService.setDeckRepository(deckRepository);
    }

    public Course createCourse(long idCourse, String nameCourse,
                               String descriptionCourse, int rating,
                               long imageId, boolean isPublished,
                               int accountId, String accountEmail, int ownerId, long categoryId){
        Course course = new Course();
        course.setName(nameCourse);
        course.setDescription(descriptionCourse);
        course.setId(idCourse);
        course.setRating(rating);
        course.setImage(new Image(imageId));
        course.setPublished(isPublished);

        User user = new User();
        Account account = new Account();
        account.setId(accountId);
        account.setEmail(accountEmail);
        user.setId(ownerId);
        user.setAccount(account);
        course.setOwner(user);

        Category category = new Category();
        category.setId(categoryId);
        course.setCategory(category);
        return course;
    }

    @Test
    public void testCoursesInPage(){
        List<Course> courseList = new ArrayList<>();

        Course course = createCourse(3L,"C# interview course",
                "questions & answers",0,
                16L,true,1,"admin@gmail.com",1,3L);

        Course course2 = createCourse(2L,"C++ interview course",
                "3 parts of java questions & answers",0,
                15L,true,1, "admin@gmail.com",1,2L);

        Course course3 = createCourse(1L,"Java interview course",
                "4 parts of java questions & answers",0,
                14L,true,1, "admin@gmail.com",1,1L);


        Course course4 = createCourse(5L,"JavaScript interview course",
                "questions & answers",0,
                18L,true,1, "admin@gmail.com",1,10L);

        Course course5 = createCourse(4L,"PHP interview course",
                "2 parts of java questions & answers",0,
                17L,true,1, "admin@gmail.com",1,4L);

        courseList.add(course);
        courseList.add(course2);
        courseList.add(course3);
        courseList.add(course4);
        courseList.add(course5);
        Page<Course> coursePage = new PageImpl<>(courseList);

        final int pageNumber = 1;
        final String sortBy = "name";
        final boolean ascending = true;

        assertEquals(courseService.getPageWithCourses(pageNumber,sortBy,ascending).getContent(), coursePage.getContent());
        assertEquals(courseService.getPageWithCourses(pageNumber,sortBy,ascending).getTotalElements(), coursePage.getTotalElements());
        assertEquals(courseService.getPageWithCourses(pageNumber,sortBy,ascending).getTotalPages(), coursePage.getTotalPages());
    }

    @Test
    public void testCoursesByCategoryAndByPage(){
        List<Course> courseList = new ArrayList<>();

        Course course = createCourse(2L, "C++ interview course",
                "3 parts of java questions & answers",0,15L,
                true,1,"admin@gmail.com",1,2L);

        courseList.add(course);

        Page<Course> coursePage = new PageImpl<>(courseList);

        final int categoryId = 2;
        final int pageNumber = 1;
        final String sortBy = "name";
        final boolean ascending = true;

        assertEquals(courseService.getPageWithCoursesByCategory(categoryId,pageNumber,sortBy,ascending).getContent(), coursePage.getContent());
        assertEquals(courseService.getPageWithCoursesByCategory(categoryId,pageNumber,sortBy,ascending).getTotalPages(), coursePage.getTotalPages());
        assertEquals(courseService.getPageWithCoursesByCategory(categoryId,pageNumber,sortBy,ascending).getTotalElements(), coursePage.getTotalElements());
    }
}