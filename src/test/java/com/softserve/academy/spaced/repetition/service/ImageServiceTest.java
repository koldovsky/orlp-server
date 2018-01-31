package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.ImageRepository;
import com.softserve.academy.spaced.repetition.service.impl.ImageServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.CanNotBeDeletedException;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class ImageServiceTest {

    private final long USER_ID = 1L;
    private final long IMAGE_ID = 1L;
    private final Long MAX_FILE_SIZE = 1_048_576L;
    private final Long USER_QUOTE = 10_485_760L;
    private final Long EXCEEDED_USER_QUOTE = USER_QUOTE + 1L;
    @InjectMocks
    private ImageServiceImpl imageService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private UserService userService;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private MessageSource messageSource;
    private User notOwnerUser;
    private Image image;

    @Before
    public void setUp() throws IOException, NotAuthorisedUserException {
        final Long NOT_OWNER_USER_ID = 42L;
        final Long IMAGE_SIZE = 1L;
        final String IMAGE_BASE64 = "base64";
        final String IMAGE_CONTENT_TYPE = "image/";
        final String MESSAGE_SOURCE_MESSAGE = "message";

        final String FIELD_MAX_FILE_SIZE = "maxFileSize";
        final String FIELD_USER_QUOTE = "userQuote";
        ReflectionTestUtils.setField(imageService, FIELD_MAX_FILE_SIZE, MAX_FILE_SIZE);
        ReflectionTestUtils.setField(imageService, FIELD_USER_QUOTE, USER_QUOTE);

        notOwnerUser = DomainFactory.createUser(NOT_OWNER_USER_ID, null, null, null, null);
        final User user = DomainFactory.createUser(USER_ID, null, null, null, null);
        image = DomainFactory.createImage(IMAGE_ID, IMAGE_BASE64, IMAGE_CONTENT_TYPE, user, IMAGE_SIZE, false);

        when(multipartFile.getSize()).thenReturn(IMAGE_SIZE);
        when(multipartFile.getBytes()).thenReturn(new byte[]{});
        when(multipartFile.getContentType()).thenReturn(IMAGE_CONTENT_TYPE);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(MESSAGE_SOURCE_MESSAGE);
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(imageRepository.findImageById(IMAGE_ID)).thenReturn(image);
        when(imageRepository.findOne(IMAGE_ID)).thenReturn(image);
        when(imageRepository.save(image)).thenReturn(image);
        doNothing().when(imageRepository).delete(image);
    }

    @Test
    public void testAddImageToDB() throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        when(imageRepository.save(any(Image.class))).thenReturn(image);
        when(imageRepository.getImageWithoutContent(null)).thenReturn(image);

        Image result = imageService.addImageToDB(multipartFile);
        verify(userService, times(2)).getAuthorizedUser();
        verify(imageRepository).save(any(Image.class));
        verify(imageRepository).getImageWithoutContent(null);
        assertEquals(image, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testAddImageToDBByNotAuthorisedUser()
            throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        imageService.addImageToDB(multipartFile);
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = ImageRepositorySizeQuotaExceededException.class)
    public void testAddImageToDBIfQuotaIsExceeded()
            throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        when(multipartFile.getSize()).thenReturn(EXCEEDED_USER_QUOTE);

        imageService.addImageToDB(multipartFile);
        verify(userService).getAuthorizedUser();
        verify(multipartFile).getSize();
    }

    @Test
    public void testCheckImageExtension() throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        imageService.checkImageExtension(multipartFile);
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testCheckImageExtensionByNotAuthorisedUser()
            throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        imageService.checkImageExtension(multipartFile);
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = ImageRepositorySizeQuotaExceededException.class)
    public void testCheckImageExtensionIfQuotaIsExceeded()
            throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        when(multipartFile.getSize()).thenReturn(EXCEEDED_USER_QUOTE);

        imageService.checkImageExtension(multipartFile);
        verify(userService).getAuthorizedUser();
        verify(multipartFile).getSize();
    }

    @Test(expected = MultipartException.class)
    public void testCheckImageExtensionIfFileIsTooLarge() throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        final Long EXCEEDED_MAX_FILE_SIZE = MAX_FILE_SIZE + 1L;

        when(multipartFile.getSize()).thenReturn(EXCEEDED_MAX_FILE_SIZE);

        imageService.checkImageExtension(multipartFile);
        verify(userService).getAuthorizedUser();
        verify(multipartFile).getSize();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckImageExtensionIfFileIsNotImage() throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        final String NOT_IMAGE_CONTENT_TYPE = "not_image";

        when(multipartFile.getContentType()).thenReturn(NOT_IMAGE_CONTENT_TYPE);

        imageService.checkImageExtension(multipartFile);
        verify(userService).getAuthorizedUser();
        verify(multipartFile).getContentType();
    }

    @Test
    public void testGetDecodedImageContentByImageId() {
        final List<Long> LIST_OF_IMAGES_ID = new ArrayList<>();
        LIST_OF_IMAGES_ID.add(IMAGE_ID);

        when(imageRepository.getIdList()).thenReturn(LIST_OF_IMAGES_ID);

        byte[] result = imageService.getDecodedImageContentByImageId(IMAGE_ID);
        verify(imageRepository).getIdList();
        verify(imageRepository).findImageById(IMAGE_ID);
        assertNotNull(result);
    }

    @Test
    public void testGetDecodeImageContentByImageIdIfImageNotExist() {
        final List<Long> LIST_OF_IMAGES_ID = new ArrayList<>();

        when(imageRepository.getIdList()).thenReturn(LIST_OF_IMAGES_ID);

        byte[] result = imageService.getDecodedImageContentByImageId(IMAGE_ID);
        verify(imageRepository).getIdList();
        assertNull(result);
    }

    @Test
    public void testEncodeToBase64() {
        String result = imageService.encodeToBase64(multipartFile);
        assertNotNull(result);
    }

    @Test
    public void testDecodeFromBase64() {
        final String ENCODED_FILE_CONTENT = "content";

        byte[] result = imageService.decodeFromBase64(ENCODED_FILE_CONTENT);
        assertNotNull(result);
    }

    @Test
    public void testGetUsersLimitsInBytesForImagesLeft() {
        when(imageRepository.getSumOfImagesSizesOfUserById(USER_ID)).thenReturn(null);

        Long result = imageService.getUsersLimitInBytesForImagesLeft(USER_ID);
        verify(imageRepository).getSumOfImagesSizesOfUserById(USER_ID);
        assertNotNull(result);
    }

    @Test
    public void testDeleteImage() throws NotAuthorisedUserException, NotOwnerOperationException, CanNotBeDeletedException {
        imageService.deleteImage(IMAGE_ID);
        verify(imageRepository).findImageById(IMAGE_ID);
        verify(userService).getAuthorizedUser();
        verify(imageRepository).delete(image);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testDeleteImageByNotAuthorisedUser() throws NotAuthorisedUserException, NotOwnerOperationException,
            CanNotBeDeletedException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        imageService.deleteImage(IMAGE_ID);
        verify(imageRepository).findImageById(IMAGE_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = NotOwnerOperationException.class)
    public void testDeleteImageByNotOwnerUser() throws NotAuthorisedUserException, NotOwnerOperationException,
            CanNotBeDeletedException {
        when(userService.getAuthorizedUser()).thenReturn(notOwnerUser);

        imageService.deleteImage(IMAGE_ID);
        verify(imageRepository).findImageById(IMAGE_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = CanNotBeDeletedException.class)
    public void testDeleteImageThatIsInUse() throws NotAuthorisedUserException, NotOwnerOperationException,
            CanNotBeDeletedException {
        image.setIsImageUsed(true);

        imageService.deleteImage(IMAGE_ID);
        verify(userService).getAuthorizedUser();
        verify(imageRepository).findImageById(IMAGE_ID);

        image.setIsImageUsed(false);
    }

    @Test
    public void testSetImageStatusInUse() {
        imageService.setImageStatusInUse(IMAGE_ID);
        verify(imageRepository).findOne(IMAGE_ID);
        verify(imageRepository).save(image);
    }

    @Test
    public void testSetImageStatusNotInUse() {
        imageService.setImageStatusNotInUse(IMAGE_ID);
        verify(imageRepository).findOne(IMAGE_ID);
        verify(imageRepository).save(image);
    }

    @Test
    public void testGetImagesForCurrentUser() throws NotAuthorisedUserException {
        when(imageRepository.getImagesWithoutContentById(USER_ID)).thenReturn(null);

        List<Image> result = imageService.getImagesForCurrentUser();
        verify(userService).getAuthorizedUser();
        verify(imageRepository).getImagesWithoutContentById(USER_ID);
        assertNull(result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetImagesForCurrentUserByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        imageService.getImagesForCurrentUser();
        verify(userService).getAuthorizedUser();
    }
}
