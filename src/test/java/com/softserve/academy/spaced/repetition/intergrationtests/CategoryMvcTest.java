package com.softserve.academy.spaced.repetition.intergrationtests;

import com.softserve.academy.spaced.repetition.Application;
import com.softserve.academy.spaced.repetition.config.SecurityConfiguration;
import com.softserve.academy.spaced.repetition.controller.CategoryController;
import com.softserve.academy.spaced.repetition.controller.CourseController;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.security.authentification.JwtUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

//@FlywayDataSource
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
@RunWith(SpringRunner.class)
//@SpringBootTest
@ActiveProfiles("test")
//@SpringBootTest
//@AutoConfigureMockMvc
//@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.yml")
public class CategoryMvcTest {

//    @Autowired
//    private WebApplicationContext context;

    private MockMvc mvc;

//    @Autowired
//    private CategoryController categoryController;

//    @Before
//    public void setup() {
//        this.mvc = standaloneSetup(this.categoryController).build();
//    }

//    @Before
//    public void setup() {
//        mvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }

//    @WithMockUser(username = "sgofforth1@tmall.com", password = "BcgSmQuhhzd")
//    @WithUserDetails("admin@gmail.com")
//    @WithMockUser(username = "name", authorities = {"ROLE_ADMIN"})
//    @WithMockUser(username = "name")
    @Test
    public void getCourseByIdTest() throws Exception {
        this.mvc.perform(get("/api/categories/{id}", 1L)).andExpect(status().isOk());
//                .with(csrf().asHeader())
//                .andExpect(status().isOk());


//        Course courseDb = repository.getCourseById(1L);
//        Course testCourse = createTestCourse();
//
//        assertEquals(courseDb.getId(),testCourse.getId());
//        assertEquals(courseDb.getName(),testCourse.getName());
//        assertEquals(courseDb.getDescription(), testCourse.getDescription());
//        assertEquals(courseDb.isPublished(), testCourse.isPublished());
    }

//    private Course createTestCourse() {
//        Course course = new Course();
//        course.setId(1L);
//        course.setName("Java interview course");
//        course.setDescription("4 parts of java questions & answers");
//        course.setPublished(true);
//        course.setCreatedBy(1L);
//        return course;
//    }
}
