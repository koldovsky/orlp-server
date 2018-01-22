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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class ImageServiceTest {

    private final long USER_ID = 1L;
    private final long NOT_OWNER_USER_ID = 42L;
    private final long IMAGE_ID = 1L;
    private final String IMAGE_BASE_64 = "";
    private final String IMAGE_CONTENT_TYPE = "image/";
    private final Long IMAGE_SIZE = 1L;
    private final String ENCODED_FILE_CONTENT = "content";
    private final Long MAX_FILE_SIZE = 1_048_576L;
    private final Long USER_QUOTA = 10_485_760L;
    @InjectMocks
    private ImageServiceImpl imageService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private UserService userService;
    @Mock
    private MultipartFile multipartFile;
    private User user;
    private User notOwnerUser;
    private Image image;

    @Before
    public void setUp() throws IOException, NotAuthorisedUserException {
        notOwnerUser = DomainFactory.createUser(NOT_OWNER_USER_ID, null, null, null, null);
        user = DomainFactory.createUser(USER_ID, null, null, null, null);
        image = DomainFactory.createImage(IMAGE_ID, IMAGE_BASE_64, IMAGE_CONTENT_TYPE, user, IMAGE_SIZE, false);

        when(multipartFile.getSize()).thenReturn(IMAGE_SIZE);
        when(multipartFile.getBytes()).thenReturn(new byte[]{});
        when(multipartFile.getContentType()).thenReturn(IMAGE_CONTENT_TYPE);
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(imageRepository.findImageById(IMAGE_ID)).thenReturn(image);
        when(imageRepository.findOne(IMAGE_ID)).thenReturn(image);
    }

    @Test
    public void testAddImageToDB() throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        image.setId(null);

        when(imageRepository.save(image)).thenReturn(image);
        when(imageRepository.getImageWithoutContent(null)).thenReturn(image);

        Image result = imageService.addImageToDB(multipartFile);
        verify(userService, times(2)).getAuthorizedUser();
        verify(imageRepository).save(image);
        verify(imageRepository).getImageWithoutContent(null);
        assertEquals(image, result);

        image.setId(IMAGE_ID);
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
        when(multipartFile.getSize()).thenReturn(USER_QUOTA + 1L);

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
        when(multipartFile.getSize()).thenReturn(USER_QUOTA + 1L);

        imageService.checkImageExtension(multipartFile);
        verify(userService).getAuthorizedUser();
        verify(multipartFile).getSize();
    }

    @Test(expected = MultipartException.class)
    public void testCheckImageExtensionIfFileIsTooLarge() throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        when(multipartFile.getSize()).thenReturn(MAX_FILE_SIZE + 1L);

        imageService.checkImageExtension(multipartFile);
        verify(userService).getAuthorizedUser();
        verify(multipartFile).getSize();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckImageExtensionIfFileIsNotImage() throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        when(multipartFile.getContentType()).thenReturn("notImage");

        imageService.checkImageExtension(multipartFile);
        verify(userService).getAuthorizedUser();
        verify(multipartFile).getContentType();
    }

    @Test
    public void testGetDecodedImageContentByImageId() {
        List<Long> idList = new ArrayList<>();
        idList.add(IMAGE_ID);

        when(imageRepository.getIdList()).thenReturn(idList);

        byte[] result = imageService.getDecodedImageContentByImageId(IMAGE_ID);
        verify(imageRepository).getIdList();
        verify(imageRepository).findImageById(IMAGE_ID);
        assertNotNull(result);
    }

    @Test
    public void testGetDecodeImageContentByImageIdIfImageNotExist() {
        when(imageRepository.getIdList()).thenReturn(new ArrayList<>());

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
        doNothing().when(imageRepository).delete(image);

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
        when(imageRepository.save(image)).thenReturn(image);

        imageService.setImageStatusInUse(IMAGE_ID);
        verify(imageRepository).findOne(IMAGE_ID);
        verify(imageRepository).save(image);
    }

    @Test
    public void testSetImageStatusNotInUse() {
        when(imageRepository.save(image)).thenReturn(image);

        imageService.setImageStatusNotInUse(IMAGE_ID);
        verify(imageRepository).findOne(IMAGE_ID);
        verify(imageRepository).save(image);
    }

    @Test
    public void testGetImagesForCurrentUser() throws NotAuthorisedUserException {
        when(imageRepository.getImagesWithoutContentById(USER_ID)).thenReturn(new ArrayList<>());

        List<Image> result = imageService.getImagesForCurrentUser();
        verify(userService).getAuthorizedUser();
        verify(imageRepository).getImagesWithoutContentById(USER_ID);
        assertNotNull(result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetImagesForCurrentUserByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        imageService.getImagesForCurrentUser();
        verify(userService).getAuthorizedUser();
    }
}
