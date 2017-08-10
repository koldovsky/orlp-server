package com.softserve.academy.spaced.repetition.service;

import com.google.api.client.util.Base64;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.CanNotBeDeletedException;
import com.softserve.academy.spaced.repetition.exceptions.ImageRepositorySizeQuotaExceededException;
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
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${app.images.maxSize}")
    private Long maxFileSize;

    @Value("${app.images.userQuote}")
    private Long userQuote;

    /**
     * Add image to the database
     *
     * @param file   - image uploaded by User
     * @param userId - id of User which have uploaded this image
     * @return
     */
    public Long addImageToDB(MultipartFile file, Long userId) throws ImageRepositorySizeQuotaExceededException {

        long fileSize = file.getSize();
        Image image = null;
        Long imageId = 0L;

        if (fileSize > getUsersLimitInBytesForImagesLeft(userId)) {
            throw new ImageRepositorySizeQuotaExceededException();
        }

        if (fileSize > maxFileSize) {
            throw new MultipartException("File upload error: file is too large.");
        } else {

            String base64 = encodeToBase64(file);
            String imageType = file.getContentType();
            User user = userRepository.findUserById(userId);
            image = new Image(base64, imageType, user, fileSize);
            imageRepository.save(image);
            imageId = image.getId();
        }
        return imageId;
    }

    /**
     * Get decoded from Base64 image content by Image Id
     *
     * @param id id of the image in the database
     * @return String, which contains decoded image content
     */
    public byte[] getDecodedImageContentByImageId(Long id) {

        byte[] imageContentet = null;
        List<Long> idList = imageRepository.getIdList();

        for (Long existingId : idList) {
            if (id.equals(existingId)) {
                Image image = imageRepository.findImageById(id);
                String encodedFileContent = image.getImagebase64();
                imageContentet = decodeFromBase64(encodedFileContent);
                break;
            }
        }
        return imageContentet;
    }


    /**
     * Encoding file-content to Base64 format
     *
     * @param file - MultiPartFile
     * @return encoded file-content
     */
    private String encodeToBase64(MultipartFile file) {
        String encodedFile = null;

        byte[] bytes = new byte[(int) file.getSize()];
        try {
            bytes = file.getBytes();
            String s = bytes.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        encodedFile = Base64.encodeBase64String(bytes);
        return encodedFile;
    }

    /**
     * Decoding String from Base64 format
     *
     * @param encodedFileContent
     * @return decoded file-content
     */
    private byte[] decodeFromBase64(String encodedFileContent) {

        return Base64.decodeBase64(encodedFileContent);
    }

    /**
     * Getting value (in bytes) of personal user's limit for uploading files to the DB.
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

    public void deleteImage(Long id, Long userId) throws CanNotBeDeletedException, NotOwnerOperationException {
        Image image = imageRepository.findImageById(id);
        Long imageOwnerId = image.getCreatedBy().getId();

        if (imageOwnerId != userId) throw new NotOwnerOperationException();

        boolean isUsed = image.isUsed();

        if (isUsed) {
            throw new CanNotBeDeletedException();
        } else {
            imageRepository.delete(image);
        }
    }
}
