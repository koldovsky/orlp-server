package com.softserve.academy.spaced.repetition.intergrationtests;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

//@SpringBootTest(classes = {Application.class, Config.class})
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//@FlywayDataSource
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@AutoConfigureMockMvc
@DataJpaTest
public class CourseRepositoryTest {

//    @Autowired
//    private MockMvc mvc;
    @Autowired
    private CourseRepository repository;

    @Test
    public void getCourseByIdTest() {
//        mvc.perform(get("/api/categories").contentType(MediaType.APPLICATION_JSON));
        Course courseDb = repository.getCourseById(1L);
        Course testCourse = createTestCourse();
//        assertEquals(courseTest.getName(), course.getName());
        assertEquals(courseDb.getId(),testCourse.getId());
        assertEquals(courseDb.getName(),testCourse.getName());
        assertEquals(courseDb.getDescription(), testCourse.getDescription());
        assertEquals(courseDb.isPublished(), testCourse.isPublished());
    }

    private Course createTestCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Java interview course");
        course.setDescription("4 parts of java questions & answers");
        course.setPublished(true);
        course.setCreatedBy(1L);
        return course;
    }
}
