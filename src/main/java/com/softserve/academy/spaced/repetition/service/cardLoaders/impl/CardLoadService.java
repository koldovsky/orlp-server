package com.softserve.academy.spaced.repetition.service.cardLoaders.impl;

import com.softserve.academy.spaced.repetition.service.cardLoaders.CardDataExtractor;
import com.softserve.academy.spaced.repetition.service.cardLoaders.CardUploader;
import com.softserve.academy.spaced.repetition.service.cardLoaders.impl.DataSaver;
import com.softserve.academy.spaced.repetition.utils.exceptions.WrongFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
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

    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    public void loadCard(MultipartFile multipartFile, Long deckId)
            throws IOException, SQLException, ClassNotFoundException, WrongFormatException {
        try{
            String relativePath = cardUploader.uploadFile(multipartFile);
            Map <String, String> map = cardDataExtractor.extractData(relativePath);
            dataSaver.save(map, deckId);
        } catch (IOException e) {
            throw new NoSuchElementException(messageSource.getMessage("message.exception.fileNotFound",
                    new Object[]{}, locale));
        } catch (SQLException e) {
            throw new WrongFormatException();
        } catch (ClassNotFoundException e) {
            throw new NoSuchElementException(messageSource.getMessage("message.exception.cardFileNotReadable",
                    new Object[]{}, locale));
        }

    }
}
