package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.utils.exceptions.CanNotBeDeletedException;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageService {

    Image addImageToDB(MultipartFile file) throws ImageRepositorySizeQuotaExceededException, NotAuthorisedUserException;

    void checkImageExtention(MultipartFile file) throws ImageRepositorySizeQuotaExceededException,
            NotAuthorisedUserException;

    byte[] getDecodedImageContentByImageId(Long id);

    String encodeToBase64(MultipartFile file);

    byte[] decodeFromBase64(String encodedFileContent);

    Long getUsersLimitInBytesForImagesLeft(Long userId);

    void deleteImage(Long id) throws CanNotBeDeletedException, NotOwnerOperationException, NotAuthorisedUserException;

    void setImageStatusInUse(Long imageId);

    void setImageStatusNotInUse(Long imageId);

    List<Image> getImagesForCurrentUser() throws NotAuthorisedUserException;
}
