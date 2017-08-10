package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.ImageDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.UploadingImageDTO;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.exceptions.CanNotBeDeletedException;
import com.softserve.academy.spaced.repetition.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
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

    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageRepository imageRepository;


    /**
     * Upload and add the image to the database
     *
     * @param file   - image-file
     * @param userId - id of the user that is adding this image to DB
     * @return - upploaded image DTO, HttpStatus
     * @throws ImageRepositorySizeQuotaExceededException - is dropping when user have exceeded the quote of disk-space for his own images
     */
    @PostMapping("/api/service/image")
    public ResponseEntity<UploadingImageDTO> addImageToDB(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) throws ImageRepositorySizeQuotaExceededException {

        Long imageId = imageService.addImageToDB(file, userId);
        Image image = imageRepository.getImageWithoutBase64(imageId);
        Link link = linkTo(methodOn(ImageController.class).getImageById(imageId)).withSelfRel();
        UploadingImageDTO uploadingimageDTO = DTOBuilder.buildDtoForEntity(image, UploadingImageDTO.class, link);
        Long bytesLeft = imageService.getUsersLimitInBytesForImagesLeft(userId);
        uploadingimageDTO.setBytesLeft(bytesLeft);

        return new ResponseEntity<>(uploadingimageDTO, HttpStatus.OK);
    }

    @GetMapping("/api/service/images/user/{userId}")
    public ResponseEntity<List<ImageDTO>> getAllImagesByUserId(@RequestParam("userId") Long userId) {

        List<Image> listId = imageRepository.getImagesWithoutBase64byId(userId);
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

        List<Image> listId = imageRepository.getImagesWithoutBase64();
        Link link = linkTo(methodOn(ImageController.class).getImageList()).withSelfRel();
        List<ImageDTO> imageDTOList = DTOBuilder.buildDtoListForCollection(listId, ImageDTO.class, link);

        return new ResponseEntity<>(imageDTOList, HttpStatus.OK);
    }

    /**
     * Delete the selected image
     *
     * @param id     - Image id, which we want to delete
     * @param userId - id of the owner of image
     * @return - Httpstatus.OK if the operation of deleting was made successfull
     * @throws CanNotBeDeletedException   - is dropping when the image which we want to delete is already in use
     * @throws NotOwnerOperationException - is dropping when the the image which we want to delete not belongs to us as to owner
     */
    @DeleteMapping(value = "/api/service/image/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") Long id, @RequestParam("userId") Long userId) throws CanNotBeDeletedException, NotOwnerOperationException {
        imageService.deleteImage(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
