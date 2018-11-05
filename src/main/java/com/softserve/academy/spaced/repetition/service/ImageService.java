package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.utils.exceptions.CanNotBeDeletedException;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Works with processing images.
 */
public interface ImageService {

    /**
     * Adds image to the database.
     *
     * @param file image uploaded by User.
     * @return saved image
     * @throws ImageRepositorySizeQuotaExceededException if user want to add image that exceed resolved size for images
     * @throws NotAuthorisedUserException                if user is not authorised
     */
    Image addImageToDB(MultipartFile file) throws ImageRepositorySizeQuotaExceededException, NotAuthorisedUserException;

    /**
     * Just check image extension.
     *
     * @param file file uploaded by User.
     * @throws ImageRepositorySizeQuotaExceededException if user want to add image that exceed resolved size for images
     * @throws NotAuthorisedUserException                if user is not authorised
     */
    void checkImageExtension(MultipartFile file) throws ImageRepositorySizeQuotaExceededException,
            NotAuthorisedUserException;

    /**
     * Gets decoded from Base64 image content for image with the given identifier.
     *
     * @param imageId must not be {@literal null}.
     * @return string, which contains decoded image content
     */
    byte[] getDecodedImageContentByImageId(Long imageId);

    /**
     * Encodes file content to Base64 format
     *
     * @param file image file
     * @return encoded file content
     */
    String encodeToBase64(MultipartFile file);

    /**
     * Decodes String from Base64 format
     *
     * @param encodedFileContent string with encoded file content
     * @return decoded file content
     */
    byte[] decodeFromBase64(String encodedFileContent);

    /**
     * Gets value (in bytes) of personal user's limit for uploading files to the DB.
     *
     * @param userId must not be {@literal null}.
     * @return number of bytes that left for uploading
     */
    Long getUsersLimitInBytesForImagesLeft(Long userId);

    /**
     * Deletes the image with with the given identifier.
     *
     * @param imageId must not be {@literal null}.
     * @throws CanNotBeDeletedException   if this image is used
     * @throws NotOwnerOperationException if user can not do this operation because it is not his image
     * @throws NotAuthorisedUserException if user is not authorised
     */
    void deleteImage(Long imageId) throws CanNotBeDeletedException, NotOwnerOperationException, NotAuthorisedUserException;

    /**
     * Sets field 'isImageUsed' on true.
     *
     * @param imageId must not be {@literal null}.
     */
    void setImageStatusInUse(Long imageId);

    /**
     * Sets field 'isImageUsed' on false.
     *
     * @param imageId must not be {@literal null}.
     */
    void setImageStatusNotInUse(Long imageId);

    /**
     * Gets images for authorized user.
     *
     * @return list of images
     * @throws NotAuthorisedUserException if user is not authorised
     */
    List<Image> getImagesForCurrentUser() throws NotAuthorisedUserException;

    /**
     * Check if user quota not exceeded, encoded string is a image and
     * encoded string size limit not exceeded.
     *
     * @param user user to which image will be added.
     * @param imageBase64 encoded string of image in base64.
     * @return return false if some check, that described above, will be false.
     * @throws ImageRepositorySizeQuotaExceededException if user quota is exceeded.
     */
    boolean isImageCanBeAddedToProfile(User user, String imageBase64) throws ImageRepositorySizeQuotaExceededException;

    List<Image> getImagesWithoutContent();
}
