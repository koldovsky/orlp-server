package com.softserve.academy.spaced.repetition.service;

import com.google.api.client.util.Base64;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    /**
     * Add image to the database
     *
     * @param file - MultiPartFile
     * @return
     */
    public Long addImageToDB(MultipartFile file){

        final long maxFileSize = 1048576L;
        long fileSize = file.getSize();
        Image image = null;
        Long imageId = 0L;

        if (fileSize > maxFileSize) {

            throw new MultipartException("File upload error: file is too large.");

        } else {

            String base64 = encodeToBase64(file);
            int hash = base64.hashCode();

            List<Integer> existingHashsList = imageRepository.getHashsList();

           for(Integer existingHash : existingHashsList){
                if ( existingHash.equals(hash)){
                    imageId = imageRepository.getIdByHash(hash);
                    return imageId;
                }

           }
            String imageType = file.getContentType();

            image = new Image(base64, hash, imageType);
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

        for (Long existingId : idList){
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
}
