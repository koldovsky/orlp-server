package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.ImageService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageControllerTest {

    private MockMvc mockMvc;
    private final Long USER_ID = 1L;
    private final Long IMAGE_ID_1 = 1L;
    private final Long IMAGE_ID_2 = 2L;
    private final Boolean IMAGE_IS_USED = true;
    private final Boolean IMAGE_IS_NOT_USED = false;
    private final String PATH_TO_IMAGE = "src/test/resources/test.jpg";

    @InjectMocks
    private ImageController imageController;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() {
        final String MESSAGE_SOURCE_MESSAGE = "message";

        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(exceptionHandlerController)
                .build();

        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(MESSAGE_SOURCE_MESSAGE);
    }

    @Test
    public void testAddImageToDB() throws Exception {
        MockMultipartFile file = createMockFile();
        when(imageService.addImageToDB(file)).thenReturn(createImage(IMAGE_ID_1, USER_ID, IMAGE_IS_USED));
        mockMvc.perform(fileUpload("/api/service/image").file(file)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageId", Matchers.is(1)))
                .andExpect(jsonPath("$.ownerId", Matchers.is(1)));
        verify(imageService, times(1)).addImageToDB(file);
    }

    @Test
    public void testGetAllImagesByUserId() throws Exception {
        List<Image> images = new ArrayList<>();
        images.add(createImage(IMAGE_ID_1, USER_ID, IMAGE_IS_USED));
        images.add(createImage(IMAGE_ID_2, USER_ID, IMAGE_IS_NOT_USED));
        when(imageService.getImagesForCurrentUser()).thenReturn(images);
        mockMvc.perform(get("/api/service/images/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].imageId", Matchers.is(1)))
                .andExpect(jsonPath("$.[0].isImageUsed", Matchers.is(true)))
                .andExpect(jsonPath("$.[1].imageId", Matchers.is(2)))
                .andExpect(jsonPath("$.[1].isImageUsed", Matchers.is(false)));
        verify(imageService, times(1)).getImagesForCurrentUser();
    }

    @Test
    public void testGetImageById() throws Exception {
        createImage(IMAGE_ID_1, USER_ID, IMAGE_IS_USED);
        byte[] imageContentBytes = Files.readAllBytes(createFile().toPath());
        when(imageService.getDecodedImageContentByImageId(IMAGE_ID_1)).thenReturn(imageContentBytes);
        mockMvc.perform(get("/api/service/image/{id}", IMAGE_ID_1)
                .accept(MediaType.IMAGE_JPEG)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().bytes(imageContentBytes))
                .andExpect(status().isOk());
        verify(imageService, times(1)).getDecodedImageContentByImageId(IMAGE_ID_1);
    }

    @Test
    public void testGetImageList() throws Exception {
        List<Image> images = new ArrayList<>();
        images.add(createImage(IMAGE_ID_1, USER_ID, IMAGE_IS_USED));
        images.add(createImage(IMAGE_ID_2, USER_ID, IMAGE_IS_NOT_USED));
        when(imageService.getImagesWithoutContent()).thenReturn(images);
        mockMvc.perform(get("/api/admin/service/image")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].imageId", Matchers.is(1)))
                .andExpect(jsonPath("$.[0].isImageUsed", Matchers.is(true)))
                .andExpect(jsonPath("$.[1].imageId", Matchers.is(2)))
                .andExpect(jsonPath("$.[1].isImageUsed", Matchers.is(false)));
        verify(imageService, times(1)).getImagesWithoutContent();
    }

    @Test
    public void testDeleteImage() throws Exception {
        mockMvc.perform(delete("/api/service/image/{id}", IMAGE_ID_1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(imageService, times(1)).deleteImage(IMAGE_ID_1);
    }

    public MockMultipartFile createMockFile() throws IOException {
        FileInputStream fis = new FileInputStream(PATH_TO_IMAGE);
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg",
                MediaType.IMAGE_JPEG_VALUE, fis);

        return file;
    }

    public File createFile() throws Exception {
        File file = new File(PATH_TO_IMAGE);

        return file;
    }

    public Image createImage(Long imageId, Long userId, Boolean isUsed) {
        Image image = new Image();
        User user = new User();
        image.setId(imageId);
        user.setId(userId);
        image.setCreatedBy(user);
        image.setIsImageUsed(isUsed);

        return image;
    }

    public User createUser(Long id) {
        User user = new User();
        user.setId(id);

        return user;
    }
}
