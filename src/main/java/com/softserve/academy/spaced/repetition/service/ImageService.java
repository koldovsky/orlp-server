package com.softserve.academy.spaced.repetition.service;

import com.google.api.client.util.Base64;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;


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

            Image image = new Image(fileName, base64, hash);
            imageRepository.save(image);
            operationStatus = OperationStatus.OK;
        }


        return operationStatus;
    }

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

    private MultipartFile decodeFromBase64(String encodedFile) {
        MultipartFile decodedFile = null;

        byte[] bytes = Base64.decodeBase64(encodedFile);


        return decodedFile;
    }

    public List<MultipartFile> getAllImages() {

        List<Image> imagesList = imageRepository.findAll();

        List<MultipartFile> filesList = null;


        return filesList;
    }

    public enum OperationStatus {
        OK, WRONG_FILE_SIZE, IMAGE_EXISTS, NAME_EXISTS;
    }
}
