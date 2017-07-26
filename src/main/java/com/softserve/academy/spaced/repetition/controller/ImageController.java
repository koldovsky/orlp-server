package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.ImageDTO;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.ImageRepository;
import com.softserve.academy.spaced.repetition.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class ImageController {


    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageController(ImageService imageService, ImageRepository imageRepository) {
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }


    /**
     * Upload and add the image to the database
     *
     * @param file - image-file
     * @return HttpStatus that depends upon result of operation
     */
    @PostMapping("/api/service/image")
    public ResponseEntity<ImageDTO> addImageToDB(@RequestParam("file") MultipartFile file){

        Long imageId = imageService.addImageToDB(file);
        Image image = imageRepository.getImageWithoutBase64(imageId);
        Link link = linkTo(methodOn(ImageController.class).getImageById(imageId)).withSelfRel();
        ImageDTO imageDTO = DTOBuilder.buildDtoForEntity(image, ImageDTO.class, link);

        return new ResponseEntity<>(imageDTO, HttpStatus.OK);
    }


    /**
     * Download image as array of bytes with getting it by id
     *
     * @param id - id of image from database
     * @return array of bytes that contain image content
     */
    @GetMapping(value = "/api/service/image/{id}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getImageById(@PathVariable("id") Long id) {

        byte[] imageContentBytes = imageService.getDecodedImageContentByImageId(id);
        if (imageContentBytes == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageContentBytes);
    }


    /**
     * Download all images from ImageRepository as list of DTO with links on on it
     *
     * @return list of ImageDTO
     */
    @GetMapping(value = "/api/service/image")
    public ResponseEntity<List<ImageDTO>> getImageList() {

        List<Image> listId = imageRepository.getImagesWithoutBase64();
        Link link = linkTo(methodOn(ImageController.class).getImageList()).withSelfRel();
        List<ImageDTO> imageDTOList = DTOBuilder.buildDtoListForCollection(listId, ImageDTO.class, link);

        return new ResponseEntity<>(imageDTOList, HttpStatus.OK);
    }
}
