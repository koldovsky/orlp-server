package com.softserve.academy.spaced.repetition.service.impl;

import com.google.api.client.util.Base64;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.ImageRepository;
import com.softserve.academy.spaced.repetition.service.ImageService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.exceptions.CanNotBeDeletedException;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.ContentType;
import java.io.IOException;
import java.util.List;


@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private UserService userService;
    @Autowired
    private ImageRepository imageRepository;

    private final Long MAX_FILE_SIZE = 1_048_576L;
    private final Long USER_QUOTA = 10_485_760L;

    @Override
    public Image addImageToDB(MultipartFile file)
            throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        User user = userService.getAuthorizedUser();
        checkImageExtension(file);
        String imageBase64 = encodeToBase64(file);
        String contentType = file.getContentType();
        Long size = file.getSize();
        Image image = new Image(imageBase64, contentType, user, size);
        imageRepository.save(image);
        Long imageId = image.getId();
        image = imageRepository.getImageWithoutContentById(imageId);
        return image;
    }

    @Override
    public void checkImageExtension(MultipartFile file)
            throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        User user = userService.getAuthorizedUser();
        Long userId = user.getId();
        long fileSize = file.getSize();
        if (fileSize > getUsersLimitInBytesForImagesLeft(userId)) {
            throw new ImageRepositorySizeQuotaExceededException();
        }
        if (fileSize > MAX_FILE_SIZE) {
            throw new MultipartException("File upload error: file is too large.");
        } else {
            String imageType = file.getContentType();
            if (imageType == null || !imageType.split("/")[0].equalsIgnoreCase("image")) {
                throw new IllegalArgumentException("File upload error: file is not an image");
            }
        }
    }

    @Override
    public byte[] getDecodedImageContentByImageId(Long imageId) {
        byte[] imageContent = null;
        List<Long> idList = imageRepository.getIdList();
        for (Long existingId : idList) {
            if (existingId.equals(imageId)) {
                Image image = imageRepository.findImageById(imageId);
                String encodedFileContent = image.getImagebase64();
                imageContent = decodeFromBase64(encodedFileContent);
                break;
            }
        }
        return imageContent;
    }

    @Override
    public String encodeToBase64(MultipartFile file) {
        long size = file.getSize();
        byte[] bytes = new byte[(int) size];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String encodedFile = Base64.encodeBase64String(bytes);
        return encodedFile;
    }

    @Override
    public byte[] decodeFromBase64(String encodedFileContent) {
        return Base64.decodeBase64(encodedFileContent);
    }

    @Override
    public Long getUsersLimitInBytesForImagesLeft(Long userId) {
        Long bytesUsed = imageRepository.getSumOfImagesSizesOfUserById(userId);
        if (bytesUsed == null) {
            bytesUsed = 0L;
        }
        Long bytesLeft = USER_QUOTA - bytesUsed;
        return bytesLeft;
    }

    @Override
    public void deleteImage(Long imageId)
            throws NotAuthorisedUserException, NotOwnerOperationException, CanNotBeDeletedException {
        User user = userService.getAuthorizedUser();
        Image image = imageRepository.findImageById(imageId);
        User imageOwner = image.getCreatedBy();
        Long imageOwnerId = imageOwner.getId();
        Long userId = user.getId();
        if (!imageOwnerId.equals(userId)) {
            throw new NotOwnerOperationException();
        }
        boolean isInUse = image.getIsImageUsed();
        if (isInUse) {
            throw new CanNotBeDeletedException();
        } else {
            imageRepository.delete(image);
        }
    }

    @Override
    public void setImageStatusInUse(Long imageId) {
        Image image = imageRepository.findOne(imageId);
        image.setIsImageUsed(true);
    }

    @Override
    public void setImageStatusNotInUse(Long imageId) {
        Image image = imageRepository.findOne(imageId);
        image.setIsImageUsed(false);
        imageRepository.save(image);
    }

    @Override
    public List<Image> getImagesForCurrentUser() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Long userId = user.getId();
        return imageRepository.getImagesWithoutContentById(userId);
    }
}
