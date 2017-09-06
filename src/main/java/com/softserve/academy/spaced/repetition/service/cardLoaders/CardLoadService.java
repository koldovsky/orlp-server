package com.softserve.academy.spaced.repetition.service.cardLoaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Service
public class CardLoadService {
    @Autowired
    private CardDataExtractor cardDataExtractor;
    @Autowired
    private ExtractedDataIntoDbSaver extractedDataIntoDbSaver;
    @Autowired
    @Qualifier("cardUploader")
    private CardUploader cardUploader;

    public Map <String, String> loadCard(MultipartFile multipartFile, Long deckId) throws IOException, SQLException, ClassNotFoundException {
        String relativePath = cardUploader.uploadFile(multipartFile);
        Map <String, String> map = cardDataExtractor.extractData(relativePath);
        extractedDataIntoDbSaver.save(map, deckId);
        return map;
    }
}
