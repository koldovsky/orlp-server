package com.softserve.academy.spaced.repetition.intergrationtests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.softserve.academy.spaced.repetition.Application;
import com.softserve.academy.spaced.repetition.config.SecurityConfiguration;
import com.softserve.academy.spaced.repetition.controller.CategoryController;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.CategoryDTO;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.service.CategoryService;
import net.minidev.json.JSONArray;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static com.jayway.jsonassert.JsonAssert.*;
import static com.jayway.jsonpath.JsonPath.read;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@DataJpaTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class FullCategoryTest {

    private static final String URL = "http://localhost:8080";

    @Autowired
    private CategoryRepository repository;

    /*
     * works
     *repository test
     */
    @Test
    public void getCategoryByIdTest() {
        Category fromDb = repository.findById(1L);
        Category testCategory = createTestCategory();
        System.out.println(testCategory.toString());

        assertEquals(fromDb.getId(), testCategory.getId());
        assertEquals(fromDb.getName(), testCategory.getName());
        assertEquals(fromDb.getDescription(), testCategory.getDescription());
    }

    private Category createTestCategory() {
        Category category = new Category();
        category.setDescription("Java publicCourses & decks");
        category.setId(1L);
        category.setName("Java");
        category.setCreatedBy(1L);
        Image image = new Image();
        image.setId(1L);
        category.setImage(image);
        return category;
    }

    /*
     * works
     * controller -> service -> repository tests without security
     */
    @Test
    public void getAllCategoriesTest() {
        ResponseEntity<String> response = new TestRestTemplate()
                .getForEntity(URL + "/api/categories", String.class);
        String json = response.getBody();
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        with(json).assertThat("$.[0].description", Matchers.is("Java publicCourses & decks"));
        with(json).assertThat("$.[0].image", Matchers.endsWith("/api/service/image/1"));
        with(json).assertThat("$.[0].createdBy", Matchers.is(1));
        with(json).assertThat("$.[0].categoryId", Matchers.is(1));
        with(json).assertThat("$.[0].name", Matchers.is("Java"));
        with(json).assertThat("$.[0]._links.courses.href",
                endsWith("/api/categories/1/courses?pageNumber=1&sortBy=id&ascending=true"));
        with(json).assertThat("$.[0]._links.decks.href",
                endsWith("/api/categories/1/decks?pageNumber=1&sortBy=id&ascending=true"));
        with(json).assertThat("$.[1].description", Matchers.is("C++ publicCourses & decks"));
        with(json).assertThat("$.[1].image", Matchers.endsWith("/api/service/image/2"));
        with(json).assertThat("$.[1].createdBy", Matchers.is(1));
        with(json).assertThat("$.[1].categoryId", Matchers.is(2));
        with(json).assertThat("$.[1].name", Matchers.is("C++"));
        with(json).assertThat("$.[1]._links.courses.href",
                endsWith("/api/categories/2/courses?pageNumber=1&sortBy=id&ascending=true"));
        with(json).assertThat("$.[1]._links.decks.href",
                endsWith("/api/categories/2/decks?pageNumber=1&sortBy=id&ascending=true"));
    }

    @Test
    public void getCatrgoryByIdTest() {
        ResponseEntity<String> response = new TestRestTemplate()
                .getForEntity(URL + "/api/categories/" + 1, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        String json = response.getBody();
        with(json).assertEquals("$.name", "Java");
        with(json).assertThat("$.image", Matchers.endsWith("/api/service/image/1"));
        with(json).assertEquals("$.description", "Java publicCourses & decks");
        with(json).assertEquals("$.createdBy", 1);
        with(json).assertEquals("$.categoryId", 1);
        with(json).assertThat("$._links.courses.href",
                endsWith("/api/categories/1/courses?pageNumber=1&sortBy=id&ascending=true"));
        with(json).assertThat("$._links.decks.href",
                endsWith("/api/categories/1/decks?pageNumber=1&sortBy=id&ascending=true"));
    }

    @Test
    public void getTopCategoriesTest() {
        ResponseEntity<String> response = new TestRestTemplate()
                .getForEntity(URL + "/api/categories/top", String.class);
        String json = response.getBody();
        JSONArray jsonArray = JsonPath.read(json, "$..name");
        assertEquals(8, jsonArray.size());
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        with(json).assertThat("$.[0].image", Matchers.endsWith("/api/service/image/1"));
        with(json).assertThat("$.[0].name", Matchers.is("Java"));
        with(json).assertThat("$.[0].description", Matchers.is("Java publicCourses & decks"));
        with(json).assertThat("$.[1].description", Matchers.is("C++ publicCourses & decks"));
        with(json).assertThat("$.[1].image", Matchers.endsWith("/api/service/image/2"));
        with(json).assertThat("$.[1].name", Matchers.is("C++"));
    }

    @Test
    public void getTopSortedCategories() {
        int numOfElements = 2;
        String sortBy = "name";
        boolean asc = true;
        ResponseEntity<String> response = new TestRestTemplate()
                .getForEntity(URL + "/api/sortedCategoriesByPage/top?p="
                        + numOfElements + "&sortBy=" + sortBy + "&asc=" + asc, String.class);
        String json = response.getBody();
        JSONArray jsonArray = JsonPath.read(json, "$..name");
        assertEquals(numOfElements, jsonArray.size());
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        with(json).assertThat("$.content.[0].name", Matchers.is("Ruby"));
        with(json).assertThat("$.content.[0].description", Matchers.is("Ruby publicCourses & decks"));
        with(json).assertThat("$.content.[0].image", Matchers.endsWith("/api/service/image/8"));
        with(json).assertThat("$.numberOfElements", Matchers.is(2));
        with(json).assertThat("$.size", Matchers.is(8));
    }

    /*
     * returns 401
     */
    @Test
    public void addCategoryTest() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("TypeScript");
        categoryDTO.setDescription("TypeScript publicCourses & decks");
        Image image = new Image();
        image.setId(10L);
        categoryDTO.setImage(image);
        HttpEntity<CategoryDTO> request = new HttpEntity<>(categoryDTO);

        ResponseEntity<String> response =
                new TestRestTemplate().postForEntity(URL + "/api/admin/categories", request, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

//        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().basicAuthorization("sgofforth1@tmall.com", "BcgSmQuhhzd");
//        restTemplateBuilder.configure(restTemplate);

//        ResponseEntity<String> resonse = new TestRestTemplate().exchange().withBasicAuth("sgofforth1@tmall.com", "BcgSmQuhhzd").postForEntity(URL + "/api/admin/categories",
//                request, String.class);
//        System.out.println(resonse.getBody());

//    @Test
//    public void addCategoryTest_() {

//        TestRestTemplate restTemplate = new TestRestTemplate();
//        HttpEntity<CategoryDTO> httpEntity = new HttpEntity<>(categoryDTO, createHeaders("sgofforth1@tmall.com", "BcgSmQuhhzd"));
//        ResponseEntity<String> result =
//                restTemplate.postForEntity(URL + "/api/admin/categories",
//                        httpEntity, String.class);
//        System.out.println(result.getStatusCode());
//

//        TestRestTemplate testRestTemplate =
//                new TestRestTemplate("sgofforth1@tmall.com", "BcgSmQuhhzd", TestRestTemplate.HttpClientOption.ENABLE_COOKIES);
//        ResponseEntity<String> responseEntity =
//                testRestTemplate.postForEntity(URL + "/api/admin/categories", request, String.class);
//        System.out.println(responseEntity.getStatusCode());

//    HttpHeaders createHeaders(String username, String password){
//        return new HttpHeaders() {{
//            String auth = username + ":" + password;
//            byte[] encodedAuth = Base64.encodeBase64(
//                    auth.getBytes(Charset.forName("US-ASCII")) );
//            String authHeader = "Authentication " + new String( encodedAuth );
//            set( "Authorization", authHeader );
//        }};
//    }
}
