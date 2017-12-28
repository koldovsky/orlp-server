package com.softserve.academy.spaced.repetition.service.cardLoaders;

import com.softserve.academy.spaced.repetition.utils.exceptions.WrongFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class CardLoadService {
    @Autowired
    private CardDataExtractor cardDataExtractor;
    @Autowired
    private DataSaver dataSaver;
    @Autowired
    @Qualifier("cardUploader")
    private CardUploader cardUploader;

    public void loadCard(MultipartFile multipartFile, Long deckId)
            throws IOException, SQLException, ClassNotFoundException, WrongFormatException {
        try{
            String relativePath = cardUploader.uploadFile(multipartFile);
            Map <String, String> map = cardDataExtractor.extractData(relativePath);
            dataSaver.save(map, deckId);
        } catch (IOException e) {
            throw new NoSuchElementException("Such file not found");
        } catch (SQLException e) {
            throw new WrongFormatException();
        } catch (ClassNotFoundException e) {
            throw new NoSuchElementException("Can't read data from uploaded file");
        }

    }
}
