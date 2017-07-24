package com.softserve.academy.spaced.repetition.service;

import com.google.api.client.util.Base64;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    /**
     * Add image to the database
     *
     * @param file     - MultiPartFile
     * @param fileName - the name of the file that was uplouded by user
     * @return
     */
    public OperationStatus addImageToDB(MultipartFile file, String fileName) {

        OperationStatus operationStatus = null;
        final long maxFileSize = 1048576L;
        long fileSize = file.getSize();

        if (fileSize > maxFileSize) {
            operationStatus = OperationStatus.WRONG_FILE_SIZE;
        } else {
            String base64 = encodeToBase64(file);
            int hash = base64.hashCode();

            List<Integer> existingHashes = imageRepository.getHashList();

            for (Integer existingHash : existingHashes) {
                if (existingHash == hash) {
                    return OperationStatus.IMAGE_EXISTS;
                }
            }

            List<String> existingNames = imageRepository.getNameList();

            for (String existingName : existingNames) {
                if (existingName.equals(fileName)) {
                    return OperationStatus.NAME_EXISTS;
                }
            }
            String imageType = file.getContentType();

            Image image = new Image(fileName, base64, hash, imageType);
            imageRepository.save(image);
            operationStatus = OperationStatus.OK;
        }


        return operationStatus;
    }

    /**
     * Get decoded from Base64 image content by Image Id
     * @param id id of the image in the database
     * @return String, which contains decoded image content
     */
    public byte[] getDecodedImageContentByImageId(Long id) {

        Image image = imageRepository.findImageById(id);
        String encodedFileContent = image.getImagebase64();
        return decodeFromBase64(encodedFileContent);

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
     * Operation status of adding image to the database
     */
    public enum OperationStatus {
        OK, WRONG_FILE_SIZE, IMAGE_EXISTS, NAME_EXISTS;
    }
}
