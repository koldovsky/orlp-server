package com.softserve.academy.spaced.repetition.config;

import com.softserve.academy.spaced.repetition.service.cardLoaders.impl.AnkiCardUploader;
import com.softserve.academy.spaced.repetition.service.cardLoaders.CardUploader;
import com.softserve.academy.spaced.repetition.service.cardLoaders.DbConnector;
import com.softserve.academy.spaced.repetition.service.cardLoaders.impl.SqliteConnector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardLoaderConfiguration {
    private static final String FILE_PATH = "src/main/resources/data/uploadedCards/card.db";

    @Bean
    @Qualifier("cardUploader")
    public CardUploader getUploader() {
        CardUploader cardUploader = new AnkiCardUploader();
        cardUploader.setFilePath(FILE_PATH);
        return cardUploader;
    }

    @Bean
    @Qualifier("cardConnector")
    public DbConnector getConnector() {
        DbConnector connector = new SqliteConnector();
        return connector;
    }
}
