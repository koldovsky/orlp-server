package com.softserve.academy.spaced.repetition.service.cardLoaders;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AnkiCardUploader implements CardUploader {
    private final static String FILEPATH = "src/main/resources/data/uploadedCards/card.db";
    private final static String DRIVER = "org.sqlite.JDBC";

    @Override
    public String uploadFile(MultipartFile file) {
        String relativePath = "";
        File newFile = new File(FILEPATH);
        try {
            newFile.createNewFile();
            relativePath = newFile.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativePath;
    }
}
