package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.ImageDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.UploadingImageDTO;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.exceptions.CanNotBeDeletedException;
import com.softserve.academy.spaced.repetition.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.repository.ImageRepository;
import com.softserve.academy.spaced.repetition.service.ImageService;
import com.softserve.academy.spaced.repetition.service.UserService;
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

    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageRepository imageRepository;

    /**
     * Upload and add the image to the database
     *
     * @param file - image-file
     * @return - upploaded image DTO, HttpStatus
     * @throws ImageRepositorySizeQuotaExceededException - is dropping when user has exceeded the quote of disk-space for his own images
     * @throws NotAuthorisedUserException                - is dropping when the user which wants to add the image is not authorised
     */
    @PostMapping("/api/service/image")
    public ResponseEntity<UploadingImageDTO> addImageToDB(@RequestParam("file") MultipartFile file) throws ImageRepositorySizeQuotaExceededException, NotAuthorisedUserException {
        Image image = imageService.addImageToDB(file);
        Long imageId = image.getId();
        Link link = linkTo(methodOn(ImageController.class).getImageById(imageId)).withSelfRel();
        UploadingImageDTO uploadingimageDTO = DTOBuilder.buildDtoForEntity(image, UploadingImageDTO.class, link);
        Long bytesLeft = imageService.getUsersLimitInBytesForImagesLeft(image.getCreatedBy().getId());
        uploadingimageDTO.setBytesLeft(bytesLeft);
        return new ResponseEntity<>(uploadingimageDTO, HttpStatus.OK);
    }

    /**
     * Gets list of imageDTOs with links on each image of defined user by his/her id
     *
     * @param userId - users id
     * @return list of imageDTOs
     */
    @GetMapping("/api/service/images/user/{userId}")
    public ResponseEntity<List<ImageDTO>> getAllImagesByUserId(@RequestParam("userId") Long userId) {
        List<Image> listId = imageRepository.getImagesWithoutContentById(userId);
        Link link = linkTo(methodOn(ImageController.class).getImageList()).withSelfRel();
        List<ImageDTO> imageDTOList = DTOBuilder.buildDtoListForCollection(listId, ImageDTO.class, link);
        return new ResponseEntity<>(imageDTOList, HttpStatus.OK);
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
        if (imageContentBytes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageContentBytes);
    }

    /**
     * Allows Admin to download all images from ImageRepository as list of DTO with links on on it
     *
     * @return list of ImageDTO
     */
    @GetMapping(value = "/api/admin/service/image")
    public ResponseEntity<List<ImageDTO>> getImageList() {
        List<Image> listId = imageRepository.getImagesWithoutContent();
        Link link = linkTo(methodOn(ImageController.class).getImageList()).withSelfRel();
        List<ImageDTO> imageDTOList = DTOBuilder.buildDtoListForCollection(listId, ImageDTO.class, link);
        return new ResponseEntity<>(imageDTOList, HttpStatus.OK);
    }

    /**
     * Delete the selected image
     *
     * @param id - Image id, which we want to delete
     * @return - HttpStatus.OK if the operation of deleting was made successfull
     * @throws NotAuthorisedUserException - is dropping when the user which wants to delete the image is not authorised
     * @throws CanNotBeDeletedException   - is dropping when the image which we want to delete is already in use
     * @throws NotOwnerOperationException - is dropping when the the image which we want to delete not belongs to us as to owner
     */
    @DeleteMapping(value = "/api/service/image/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") Long id) throws CanNotBeDeletedException, NotOwnerOperationException, NotAuthorisedUserException {
        imageService.deleteImage(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
