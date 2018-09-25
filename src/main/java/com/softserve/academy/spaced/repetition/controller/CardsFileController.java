package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.service.CardService;
import com.softserve.academy.spaced.repetition.utils.exceptions.EmptyFileException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.utils.exceptions.WrongFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CardsFileController {

    @Autowired
    private CardService cardService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("api/upload/deck/{deckId}/cards")
    public void uploadFile(@RequestParam("file") MultipartFile cardsFile, @PathVariable Long deckId)
            throws WrongFormatException, NotOwnerOperationException,
            NotAuthorisedUserException, EmptyFileException, IOException {
        cardService.uploadCards(cardsFile, deckId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/download/deck/{deckId}/cards")
    public void downloadFile(HttpServletResponse response, @PathVariable Long deckId) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=Cards.yml");
        cardService.downloadCards(deckId, response.getOutputStream());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/download/template/cards")
    public void downloadFileTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=CardsTemplate.yml");
        cardService.downloadCardsTemplate(response.getOutputStream());
    }
}
