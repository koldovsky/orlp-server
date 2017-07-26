package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.exceptions.ImageContextDublicationException;
import com.softserve.academy.spaced.repetition.exceptions.ImageNameDublicationException;
import com.softserve.academy.spaced.repetition.service.ImageService;
import com.softserve.academy.spaced.repetition.service.ImageService.OperationStatus;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class ImageController {


    @Autowired
    private ImageService imageService;

    /**
     * Upload and add the image to the database
     *
     * @param fileName - the name of the file that was uplouded by user
     * @param file     - image-file
     * @return HttpStatus that depends upon result of operation
     * @throws ImageContextDublicationException
     * @throws ImageNameDublicationException
     */
    @PostMapping("/api/service/image/")
    public ResponseEntity<?> addImageToDB(@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file) throws ImageContextDublicationException, ImageNameDublicationException {

        HttpStatus httpStatus = HttpStatus.CONFLICT;

        OperationStatus operationStatus = imageService.addImageToDB(file, fileName);

        switch (operationStatus) {
            case OK:
                httpStatus = HttpStatus.OK;
                break;
            case IMAGE_EXISTS:
                throw new ImageContextDublicationException();
            case WRONG_FILE_SIZE:
                throw new MultipartException("File upload error: file is too large.");
            case NAME_EXISTS:
                throw new ImageNameDublicationException();
        }

        return new ResponseEntity<>(httpStatus);
    }

    /**
     * Download image as array of bytes with getting by id
     *
     * @param id - id of image from database
     * @return array of bytes that contain image content
     */
    @GetMapping(value = "/api/service/image/id/{id}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getImageById(@RequestParam("id") Long id) {

        byte[] imageContentBytes = imageService.getDecodedImageContentByImageId(id);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageContentBytes);
    }
}
