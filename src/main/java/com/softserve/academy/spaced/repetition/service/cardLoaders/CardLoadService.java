package com.softserve.academy.spaced.repetition.service.cardLoaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CardLoadService {
    @Autowired
    private AnkiCardExtractor cardExtractor;
    @Autowired
    private AnkiCardUploader cardUploader;

    public Map <String, String> loadCard(MultipartFile multipartFile) {
        String relativePath = cardUploader.uploadFile(multipartFile);
        Map <String, String> map = cardExtractor.extractData(relativePath);
        return map;
    }
}
