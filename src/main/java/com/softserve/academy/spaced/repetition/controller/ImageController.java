package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.ImageDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.UploadingImageDTO;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.ImageRepository;
import com.softserve.academy.spaced.repetition.service.ImageService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.CanNotBeDeletedException;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoListForCollection;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class ImageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseCommentController.class);

    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageRepository imageRepository;

    /**
     * Upload and add the image to the database
     *
     * @param file - image-file
     * @return - uploaded image dto,
     * @throws ImageRepositorySizeQuotaExceededException - is dropping when user has exceeded the quote of disk-space
     *                                                   for his own images
     * @throws NotAuthorisedUserException                - is dropping when the user which wants to add the image is
     *                                                   not authorised
     */
    @Auditable(action = AuditingAction.UPLOAD_IMAGE)
    @PostMapping("/api/service/image")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('IMAGE','UPDATE')")
    public ResponseEntity<UploadingImageDTO> addImageToDB(@RequestParam("file") MultipartFile file)
            throws ImageRepositorySizeQuotaExceededException, NotAuthorisedUserException {
        LOGGER.debug("Adding image to DB");
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
     * @return list of imageDTOs
     */
    @GetMapping("/api/service/images/user")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('IMAGE','READ')")
    public List<ImageDTO> getAllImagesByUserId() throws NotAuthorisedUserException {
        List<Image> listId = imageService.getImagesForCurrentUser();
        return buildDtoListForCollection(listId, ImageDTO.class,
                linkTo(methodOn(ImageController.class).getImageList()).withSelfRel());
    }

    /**
     * Download image as array of bytes with getting it by id
     *
     * @param id - id of image from database
     * @return array of bytes that contain image content
     */
    @GetMapping(value = "/api/service/image/{id}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("hasPermission('IMAGE','READ') || !isAuthenticated()")
    public ResponseEntity<byte[]> getImageById(@PathVariable("id") Long id) {
        byte[] imageContentBytes = imageService.getDecodedImageContentByImageId(id);
        if (imageContentBytes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageContentBytes);
    }

    /**
     * Allows Admin to download all images from ImageService as list of dto with links on on it
     *
     * @return list of ImageDTO
     */
    @Auditable(action = AuditingAction.VIEW_ALL_IMAGE_ADMIN)
    @GetMapping(value = "/api/admin/service/image")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('IMAGE','READ')")
    public List<ImageDTO> getImageList() {
        List<Image> listId = imageService.getImagesWithoutContent();
        return buildDtoListForCollection(listId, ImageDTO.class,
                linkTo(methodOn(ImageController.class).getImageList()).withSelfRel());
    }

    /**
     * Delete the selected imagelinkTo(methodOn(ImageController.class).getImageList()).withSelfRel()
     *
     * @param id - Image id, which we want to delete
     * @return - HttpStatus.OK if the operation of deleting was made successfull
     * @throws NotAuthorisedUserException - is dropping when the user which wants to delete the image is not authorised
     * @throws CanNotBeDeletedException   - is dropping when the image which we want to delete is already in use
     * @throws NotOwnerOperationException - is dropping when the the image which we want to delete not belongs to us as
     *                                    to owner
     */
    @Auditable(action = AuditingAction.DELETE_IMAGE)
    @DeleteMapping(value = "/api/service/image/{id}")
    @PreAuthorize("hasPermission('IMAGE','DELETE')")
    public ResponseEntity<?> deleteImage(@PathVariable("id") Long id) throws CanNotBeDeletedException,
            NotOwnerOperationException, NotAuthorisedUserException {
        LOGGER.debug("Deleting image with id: {}", id);
        imageService.deleteImage(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
