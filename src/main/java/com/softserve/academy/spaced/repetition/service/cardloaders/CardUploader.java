package com.softserve.academy.spaced.repetition.service.cardloaders;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface CardUploader {
    void setFilePath(String filePath);

    String uploadFile(MultipartFile file) throws IOException;
}
