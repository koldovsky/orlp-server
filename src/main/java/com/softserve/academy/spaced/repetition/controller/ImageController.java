package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin
public class ImageController {


    @Autowired
    private ImageService imageService;


    @PostMapping("/api/service/image/")
    public ResponseEntity<?> addImageToDB (@RequestParam("fileName") String fileName, @RequestParam("file")MultipartFile file) throws IOException {

        imageService.addImageToDB(file, fileName);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
