package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.exceptions.EmptyFileException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.exceptions.WrongFormatException;
import com.softserve.academy.spaced.repetition.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class CardsFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardsFileController.class);

    @Autowired
    private CardService cardService;

    @PostMapping("api/private/upload/deck/{deckId}/cards")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile cardsFile, @PathVariable Long deckId) throws WrongFormatException, NotOwnerOperationException, NotAuthorisedUserException, EmptyFileException, IOException {
        cardService.uploadCards(cardsFile, deckId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("download/deck/{deckId}/cards")
    public StreamingResponseBody downloadFile(HttpServletResponse response, @PathVariable Long deckId) {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=Cards.yml");
        return outputStream -> {
            outputStream.write("cards:".getBytes());
            cardService.downloadCards(deckId).forEach(card -> {
                try {
                    outputStream.write(String.format("%n - title: %s", card.getTitle()).getBytes());
                    outputStream.write(String.format("%n   question: %s", card.getQuestion()).getBytes());
                    outputStream.write(String.format("%n   answer: > %n%-12s%s", " ", card.getAnswer()).getBytes());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            });
            outputStream.flush();
        };
    }

    @GetMapping("api/private/download/template/cards")
    public void downloadFileTemplate(HttpServletResponse response) throws IOException{
        try (InputStream in = CardsFileController.class.getResourceAsStream("/data/CardsTemplate.yml")) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=CardsTemplate.yml");
            FileCopyUtils.copy(in, response.getOutputStream());
        }
    }
}
