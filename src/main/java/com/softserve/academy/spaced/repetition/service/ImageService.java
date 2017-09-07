package com.softserve.academy.spaced.repetition.service;

import com.google.api.client.util.Base64;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.CanNotBeDeletedException;
import com.softserve.academy.spaced.repetition.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.repository.ImageRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Service for processing images
 */
@Service
public class ImageService {
    @Autowired
    private UserService userService;
    @Autowired
    private ImageRepository imageRepository;
    @Value("${app.images.maxSize}")
    private Long maxFileSize;
    @Value("${app.images.userQuote}")
    private Long userQuote;

    /**
     * Adds image to the database
     *
     * @param file - image uploaded by User
     * @return
     * @throws ImageRepositorySizeQuotaExceededException - is dropping when user has exceeded the quote of disk-space for his own images
     * @throws NotAuthorisedUserException                - is dropping when the user which wants to add the image is not authorised
     */
    public Image addImageToDB(MultipartFile file) throws ImageRepositorySizeQuotaExceededException, NotAuthorisedUserException {
        Image image = null;
        long fileSize = file.getSize();
        User user = userService.getAuthorizedUser();
        if (fileSize > getUsersLimitInBytesForImagesLeft(user.getId())) {
            throw new ImageRepositorySizeQuotaExceededException();
        }
        if (fileSize > maxFileSize) {
            throw new MultipartException("File upload error: file is too large.");
        } else {
            String base64 = encodeToBase64(file);
            String imageType = file.getContentType();
            image = new Image(base64, imageType, user, fileSize);
            imageRepository.save(image);
            image = imageRepository.getImageWithoutContent(image.getId());
        }
        return image;
    }

    /**
     * Gets decoded from Base64 image content by Image Id
     *
     * @param id id of the image in the database
     * @return String, which contains decoded image content
     */
    public byte[] getDecodedImageContentByImageId(Long id) {
        byte[] imageContent = null;
        List<Long> idList = imageRepository.getIdList();
        for (Long existingId : idList) {
            if (id.equals(existingId)) {
                Image image = imageRepository.findImageById(id);
                String encodedFileContent = image.getImagebase64();
                imageContent = decodeFromBase64(encodedFileContent);
                break;
            }
        }
        return imageContent;
    }

    /**
     * Encodes file-content to Base64 format
     *
     * @param file - MultiPartFile
     * @return encoded file-content
     */
    private String encodeToBase64(MultipartFile file) {
        String encodedFile = null;
        byte[] bytes = new byte[(int) file.getSize()];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        encodedFile = Base64.encodeBase64String(bytes);
        return encodedFile;
    }

    /**
     * Decodes String from Base64 format
     *
     * @param encodedFileContent
     * @return decoded file-content
     */
    private byte[] decodeFromBase64(String encodedFileContent) {

        return Base64.decodeBase64(encodedFileContent);
    }

    /**
     * Gets value (in bytes) of personal user's limit for uploading files to the DB.
     *
     * @param userId - user's id
     * @return number of bytes that left to upload
     */
    public Long getUsersLimitInBytesForImagesLeft(Long userId) {

        Long bytesUsed = imageRepository.getSumOfImagesSizesOfUserById(userId);
        if (bytesUsed == null) {
            bytesUsed = 0L;
        }
        Long bytesLeft = userQuote - bytesUsed;
        return bytesLeft;
    }

    /**
     * Deletes the image with determined id
     *
     * @param id - id of the image we would like to delete
     * @throws CanNotBeDeletedException   - is dropping when the image which we want to delete is already in use
     * @throws NotOwnerOperationException - is dropping when the image which we want to delete is already in use
     * @throws NotAuthorisedUserException - is dropping when the user which wants to delete the image is not authorised
     */
    public void deleteImage(Long id) throws CanNotBeDeletedException, NotOwnerOperationException, NotAuthorisedUserException {
        Image image = imageRepository.findImageById(id);
        Long imageOwnerId = image.getCreatedBy().getId();
        Long userId = 0L;
        userId = userService.getAuthorizedUser().getId();
        if (imageOwnerId != userId) {
            throw new NotOwnerOperationException();
        }
        boolean isUsed = image.isUsed();
        if (isUsed) {
            throw new CanNotBeDeletedException();
        } else {
            imageRepository.delete(image);
        }
    }

    /**
     * Sets image status to "in use"
     *
     * @param imageId
     */
    public void setImageStatusInUse(Long imageId) {
        Image image = imageRepository.findOne(imageId);
        image.setUsed(true);
        imageRepository.save(image);
    }

    /**
     * Sets image status to "not in use"
     *
     * @param imageId
     */
    public void setImageStatusNotInUse(Long imageId) {
        Image image = imageRepository.findOne(imageId);
        image.setUsed(false);
        imageRepository.save(image);
    }

    /**
     * Gets images for authorized user
     *
     * @return List of images
     */
    public List<Image> getImagesForCurrentUser() throws NotAuthorisedUserException {
        Long userId = userService.getAuthorizedUser().getId();
        return imageRepository.getImagesWithoutContentById(userId);
    }
}
