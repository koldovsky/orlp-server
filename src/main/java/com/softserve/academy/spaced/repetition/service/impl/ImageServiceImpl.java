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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private UserService userService;
    @Autowired
    private ImageRepository imageRepository;
    @Value("${app.images.maxSize}")
    private Long maxFileSize;
    @Value("${app.images.userQuote}")
    private Long userQuote;

    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    @Override
    public Image addImageToDB(MultipartFile file)
            throws ImageRepositorySizeQuotaExceededException, NotAuthorisedUserException {
        checkImageExtension(file);
        Image image = new Image(encodeToBase64(file), file.getContentType(),
                userService.getAuthorizedUser(), file.getSize());
        imageRepository.save(image);
        image = imageRepository.getImageWithoutContent(image.getId());
        return image;
    }


    @Override
    public void checkImageExtension(MultipartFile file) throws ImageRepositorySizeQuotaExceededException,
            NotAuthorisedUserException {
        long fileSize = file.getSize();
        User user = userService.getAuthorizedUser();
        if (fileSize > getUsersLimitInBytesForImagesLeft(user.getId())) {
            throw new ImageRepositorySizeQuotaExceededException();
        }
        if (fileSize > maxFileSize) {
            throw new MultipartException(messageSource.getMessage("message.exception.fileSizeTooLarge",
                    new Object[]{}, locale));
        } else {
            String imageType = file.getContentType();
            if (imageType == null || !imageType.split("/")[0].equalsIgnoreCase("image")) {
                throw new IllegalArgumentException(messageSource.getMessage("message.exception.imageFileWrongFormat",
                        new Object[]{}, locale));
            }
        }
    }

    @Override
    public boolean isImageCanBeAddedToProfile(User user, String imageBase64) throws ImageRepositorySizeQuotaExceededException {
        if(isUserImageQuotaExceeded(user)) {
            throw new ImageRepositorySizeQuotaExceededException();
        }
        if(isEncodedStringNotImage(imageBase64)) {
           throw new IllegalArgumentException(messageSource.getMessage(("message.exception.imageFileWrongFormat"),
                   new Object[]{}, locale));
        }
        if(isEncodedStringExceedMaxSize(imageBase64)) {
            throw new MultipartException(messageSource.getMessage(("message.exception.fileSizeTooLarge"),
                    new Object[]{}, locale));
        }
        return true;
    }

    private boolean isUserImageQuotaExceeded(User user) {
        return getUsersLimitInBytesForImagesLeft(user.getId()) > userQuote;
    }

    private boolean isEncodedStringNotImage(String imageBase64) {
        return !imageBase64.startsWith("data:image/");
    }

    private boolean isEncodedStringExceedMaxSize(String imageBase64) {
        return imageBase64.length() > maxFileSize;
    }

    @Override
    public byte[] getDecodedImageContentByImageId(Long id) {
        byte[] imageContent = null;
        List<Long> idList = imageRepository.getIdList();
        for (Long existingId : idList) {
            if (id.equals(existingId)) {
                Image image = imageRepository.findImageById(id);
                String encodedFileContent = image.getImageBase64();
                imageContent = decodeFromBase64(encodedFileContent);
                break;
            }
        }
        return imageContent;
    }


    @Override
    public String encodeToBase64(MultipartFile file) {
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
        Long bytesLeft = userQuote - bytesUsed;
        return bytesLeft;
    }


    @Override
    public void deleteImage(Long id)
            throws CanNotBeDeletedException, NotOwnerOperationException, NotAuthorisedUserException {
        Image image = imageRepository.findImageById(id);
        Long imageOwnerId = image.getCreatedBy().getId();
        Long userId = 0L;
        userId = userService.getAuthorizedUser().getId();
        if (imageOwnerId != userId) {
            throw new NotOwnerOperationException();
        }
        boolean isUsed = image.getIsImageUsed();
        if (isUsed) {
            throw new CanNotBeDeletedException();
        } else {
            imageRepository.delete(image);
        }
    }


    @Override
    public void setImageStatusInUse(Long imageId) {
        Image image = imageRepository.findOne(imageId);
        image.setIsImageUsed(true);
        imageRepository.save(image);
    }


    @Override
    public void setImageStatusNotInUse(Long imageId) {
        Image image = imageRepository.findOne(imageId);
        image.setIsImageUsed(false);
        imageRepository.save(image);
    }

    @Override
    public List<Image> getImagesForCurrentUser() throws NotAuthorisedUserException {
        Long userId = userService.getAuthorizedUser().getId();
        return imageRepository.getImagesWithoutContentById(userId);
    }

    @Override
    public List<Image> getImagesWithoutContent() {
        List<Image> images = imageRepository.getImagesWithoutContent();
        return images;
    }
}
