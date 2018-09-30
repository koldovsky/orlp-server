package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckOfUserManagedByAdminDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.UserManagedByAdminDTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoForEntity;
import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoListForCollection;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("api/admin/users")
public class ManageUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseCommentController.class);

    @Autowired
    private UserService userService;

    /**
     * Gets the list of all users
     *
     * @return list of managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.VIEW_ALL_USERS_ADMIN)
    @GetMapping
    @PreAuthorize("hasPermission('MANAGE_USER','READ')")
    public ResponseEntity<Page<UserManagedByAdminDTO>> getAllUsers(@RequestParam(name = "p", defaultValue = "1")
                                                                           int pageNumber,
                                                                   @RequestParam(name = "sortBy") String sortBy,
                                                                   @RequestParam(name = "asc") boolean ascending) {
        Page<UserManagedByAdminDTO> userManagedByAdminDTOS = userService
                .getUsersByPage(pageNumber, sortBy, ascending).map(user -> {
                    Link selfLink = linkTo(methodOn(ManageUserController.class).getUserById(user.getId())).withSelfRel();
                    return buildDtoForEntity(user, UserManagedByAdminDTO.class, selfLink);
                });
        return new ResponseEntity<>(userManagedByAdminDTOS, HttpStatus.OK);
    }

    /**
     * Gets user by id
     *
     * @param id - users id
     * @return managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.VIEW_ONE_USER_ADMIN)
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('MANAGE_USER','READ')")
    public UserManagedByAdminDTO getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return buildDtoForEntity(user, UserManagedByAdminDTO.class,
                linkTo(methodOn(ManageUserController.class).getUserById(id)).withSelfRel());
    }

    /**
     * Sets users status "Blocked"
     *
     * @param id - users id
     * @return managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.SET_ACCOUNT_BLOCKED)
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('MANAGE_USER','UPDATE')")
    public UserManagedByAdminDTO setUsersStatusBlocked(@PathVariable Long id) {
        User userWithChangedStatus = userService.setUsersStatusBlocked(id);
        return buildDtoForEntity(userWithChangedStatus, UserManagedByAdminDTO.class,
                linkTo(methodOn(ManageUserController.class).setUsersStatusBlocked(id)).withSelfRel());
    }

    /**
     * Sets users status "Deleted"
     *
     * @param id - users id
     * @return managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.SET_ACCOUNT_DELETED)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('MANAGE_USER','DELETE')")
    public UserManagedByAdminDTO setUsersStatusDeleted(@PathVariable Long id) {
        User userWithChangedStatus = userService.setUsersStatusDeleted(id);
        return buildDtoForEntity(userWithChangedStatus, UserManagedByAdminDTO.class,
                linkTo(methodOn(ManageUserController.class).setUsersStatusDeleted(id)).withSelfRel());
    }

    /**
     * Sets users status "Active"
     *
     * @param id - users id
     * @return managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.SET_ACCOUNT_ACTIVE)
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('MANAGE_USER','UPDATE')")
    public UserManagedByAdminDTO setUsersStatusActive(@PathVariable Long id) {
        User userWithChangedStatus = userService.setUsersStatusActive(id);
        return buildDtoForEntity(userWithChangedStatus, UserManagedByAdminDTO.class,
                linkTo(methodOn(ManageUserController.class).setUsersStatusActive(id)).withSelfRel());
    }

    /**
     * Adds deck to users folder
     *
     * @param userId - users id
     * @param deckId - decks id
     * @return managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.ADD_DECK_TO_USER_FOLDER_ADMIN)
    @PostMapping("/{userId}/deck/{deckId}")
    @PreAuthorize("hasPermission('MANAGE_USER','UPDATE')")
    public ResponseEntity<UserManagedByAdminDTO> addExistingDeckToUsersFolder(@PathVariable("userId") Long userId,
                                                                              @PathVariable("deckId") Long deckId) {
        LOGGER.debug("Adding existing deck with id: {} to user folder with id: {}", deckId, userId);
        User user = userService.addExistingDeckToUsersFolder(userId, deckId);
        if (user != null) {
            Link link = linkTo(methodOn(ManageUserController.class)
                    .addExistingDeckToUsersFolder(userId, deckId)).withSelfRel();
            UserManagedByAdminDTO userManagedByAdminDTO =
                    buildDtoForEntity(user, UserManagedByAdminDTO.class, link);
            return new ResponseEntity<>(userManagedByAdminDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes deck from users folder
     *
     * @param userId - users id
     * @param deckId - decks id
     * @return managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.REMOVE_DECK_FROM_USER_FOLDER_ADMIN)
    @DeleteMapping("/{userId}/deck/{deckId}")
    @PreAuthorize("hasPermission('MANAGE_USER','DELETE')")
    public ResponseEntity<UserManagedByAdminDTO> removeDeckFromUsersFolder(@PathVariable("userId") Long userId,
                                                                           @PathVariable("deckId") Long deckId) {
        LOGGER.debug("Deleting deck with id: {} from user folder with id: {}", deckId, userId);
        User user = userService.removeDeckFromUsersFolder(userId, deckId);
        if (user != null) {
            Link link = linkTo(methodOn(ManageUserController.class).getUserById(userId)).withSelfRel();
            UserManagedByAdminDTO userManagedByAdminDTO =
                    buildDtoForEntity(user, UserManagedByAdminDTO.class, link);
            return new ResponseEntity<UserManagedByAdminDTO>(userManagedByAdminDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets list of decks from the folder of the defined user
     *
     * @param userId - users id
     * @return managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.VIEW_FOLDER_DECKS_ADMIN)
    @GetMapping("/{userId}/decks")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('MANAGE_USER','READ')")
    public List<DeckOfUserManagedByAdminDTO> getAllDecksFromUsersFolder(@PathVariable("userId") Long userId) {
        List<Deck> decksFromUsersFolder = userService.getAllDecksFromUsersFolder(userId);
        return buildDtoListForCollection(decksFromUsersFolder, DeckOfUserManagedByAdminDTO.class,
                linkTo(methodOn(ManageUserController.class).getAllDecksFromUsersFolder(userId)).withSelfRel());
    }
}
