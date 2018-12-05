package com.softserve.academy.spaced.repetition.service.cardloaders.impl;

import com.softserve.academy.spaced.repetition.service.cardloaders.CardUploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AnkiCardUploader implements CardUploader {
    private String filePath;

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String relativePath = "";
        File newFile = new File(filePath);
        newFile.createNewFile();
        relativePath = newFile.getAbsolutePath();
        FileOutputStream fos = new FileOutputStream(newFile);
        fos.write(file.getBytes());
        fos.close();
        return relativePath;
    }
}
