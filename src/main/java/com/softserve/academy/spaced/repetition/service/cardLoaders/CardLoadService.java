package com.softserve.academy.spaced.repetition.service.cardLoaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;
import java.util.Map;

@Service
public class CardLoadService {
    @Autowired
    private AnkiCardExtractor cardExtractor;
    @Autowired
    private AnkiCardUploader cardUploader;

    public void loadCard(MultipartFile multipartFile) {
        String relativePath = cardUploader.uploadFile(multipartFile);
        Map <String, String> map = cardExtractor.extractData(relativePath);
        Iterator <Map.Entry <String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry <String, String> elem = iterator.next();
            System.out.println(elem.getKey() + " >>>>>>>>>>>>>>> ");
            System.out.print(elem.getValue());
            System.out.println();
        }
    }
}
