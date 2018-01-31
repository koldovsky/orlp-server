package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.service.CardService;
import com.softserve.academy.spaced.repetition.service.cardLoaders.impl.CardLoadService;
import com.softserve.academy.spaced.repetition.utils.exceptions.EmptyFileException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.utils.exceptions.WrongFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Controller
public class CardsFileController {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardLoadService cardLoadService;

    @PostMapping("api/private/upload/deck/{deckId}/cards")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile cardsFile, @PathVariable Long deckId)
            throws WrongFormatException,NotOwnerOperationException, NotAuthorisedUserException, EmptyFileException,
            IOException {
        cardService.uploadCards(cardsFile, deckId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("api/private/download/deck/{deckId}/cards")
    public void downloadFile(HttpServletResponse response, @PathVariable Long deckId) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=Cards.yml");
        cardService.downloadCards(deckId, response.getOutputStream());
    }

    @GetMapping("api/private/download/template/cards")
    public void downloadFileTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=CardsTemplate.yml");
        cardService.downloadCardsTemplate(response.getOutputStream());
    }

    //TODO remove!
    /**
     * Upload anki cards
     *
     * @param file - card-file
     * @return - HttpStatus.Ok
     * @throws NoSuchElementException - is dropping when classloader failed in loading Driver to uploading file.
     * @throws WrongFormatException   - is dropping when uploading file has wrong format.
     * @throws NoSuchElementException - is dropping when file is not found.
     */
    @PostMapping("/api/cardsUpload")
    public ResponseEntity uploadCard(@RequestParam("file") MultipartFile file, Long deckId)
            throws IOException, SQLException, ClassNotFoundException, WrongFormatException {
        cardLoadService.loadCard(file, deckId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
