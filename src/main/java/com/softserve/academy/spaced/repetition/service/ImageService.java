package com.softserve.academy.spaced.repetition.service;

import com.google.api.client.util.Base64;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;


    public void addImageToDB(MultipartFile file, String fileName) {
        String base64 = encodeToBase64(file);
        Image image = new Image(fileName, base64);
        imageRepository.save(image);
    }

    private String encodeToBase64(MultipartFile file) {
        String encodedFile = null;
        System.out.println(file.getSize());

        byte[] bytes = new byte[(int) file.getSize()];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        encodedFile = Base64.encodeBase64String(bytes);



        return encodedFile;
    }
}
