package com.softserve.academy.spaced.repetition.service.cardLoaders;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CardUploader {
    String uploadFile(MultipartFile file);
}
