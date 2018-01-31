package com.softserve.academy.spaced.repetition.service.cardLoaders.impl;

import com.softserve.academy.spaced.repetition.service.cardLoaders.CardUploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AnkiCardUploader implements CardUploader {
    private String FILE_PATH;

    @Override
    public void setFilePath(String filePath) {
        this.FILE_PATH = filePath;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String relativePath = "";
        File newFile = new File(FILE_PATH);
        newFile.createNewFile();
        relativePath = newFile.getAbsolutePath();
        FileOutputStream fos = new FileOutputStream(newFile);
        fos.write(file.getBytes());
        fos.close();
        return relativePath;
    }
}
