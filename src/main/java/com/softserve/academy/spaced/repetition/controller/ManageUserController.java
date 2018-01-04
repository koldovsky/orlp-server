package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.utils.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.DeckOfUserManagedByAdminDTO;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.UserManagedByAdminDTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class ManageUserController {

    @Autowired
    private UserService userService;

    /**
     * Gets the list of all users
     *
     * @return list of managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.VIEW_ALL_USERS_ADMIN)
    @GetMapping("/api/admin/users")
    public ResponseEntity<Page<UserManagedByAdminDTO>> getAllUsers(@RequestParam(name = "p", defaultValue = "1")
                                                                           int pageNumber,
                                                                   @RequestParam(name = "sortBy") String sortBy,
                                                                   @RequestParam(name = "asc") boolean ascending) {
        Page<UserManagedByAdminDTO> userManagedByAdminDTOS = userService
                .getUsersByPage(pageNumber, sortBy, ascending).map((user) -> {
                    Link selfLink = linkTo(methodOn(ManageUserController.class).getUserById(user.getId())).withSelfRel();
                    return DTOBuilder.buildDtoForEntity(user, UserManagedByAdminDTO.class, selfLink);
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
    @GetMapping("/api/admin/users/{id}")
    public ResponseEntity<UserManagedByAdminDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        Link link = linkTo(methodOn(ManageUserController.class).getUserById(id)).withSelfRel();
        UserManagedByAdminDTO userDTO = DTOBuilder.buildDtoForEntity(user, UserManagedByAdminDTO.class, link);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /**
     * Sets users status "Blocked"
     *
     * @param id - users id
     * @return managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.SET_ACCOUNT_BLOCKED)
    @PutMapping("/api/admin/users/{id}")
    public ResponseEntity<UserManagedByAdminDTO> setUsersStatusBlocked(@PathVariable Long id) {
        User userWithChangedStatus = userService.setUsersStatusBlocked(id);
        Link link = linkTo(methodOn(ManageUserController.class).setUsersStatusBlocked(id)).withSelfRel();
        UserManagedByAdminDTO userManagedByAdminDTO = DTOBuilder
                .buildDtoForEntity(userWithChangedStatus, UserManagedByAdminDTO.class, link);
        return new ResponseEntity<>(userManagedByAdminDTO, HttpStatus.OK);
    }

    /**
     * Sets users status "Deleted"
     *
     * @param id - users id
     * @return managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.SET_ACCOUNT_DELETED)
    @DeleteMapping("/api/admin/users/{id}")
    public ResponseEntity<UserManagedByAdminDTO> setUsersStatusDeleted(@PathVariable Long id) {
        User userWithChangedStatus = userService.setUsersStatusDeleted(id);
        Link link = linkTo(methodOn(ManageUserController.class).setUsersStatusDeleted(id)).withSelfRel();
        UserManagedByAdminDTO userManagedByAdminDTO = DTOBuilder
                .buildDtoForEntity(userWithChangedStatus, UserManagedByAdminDTO.class, link);
        return new ResponseEntity<>(userManagedByAdminDTO, HttpStatus.OK);
    }

    /**
     * Sets users status "Active"
     *
     * @param id - users id
     * @return managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.SET_ACCOUNT_ACTIVE)
    @PostMapping("/api/admin/users/{id}")
    public ResponseEntity<UserManagedByAdminDTO> setUsersStatusActive(@PathVariable Long id) {
        User userWithChangedStatus = userService.setUsersStatusActive(id);
        Link link = linkTo(methodOn(ManageUserController.class).setUsersStatusActive(id)).withSelfRel();
        UserManagedByAdminDTO userManagedByAdminDTO = DTOBuilder
                .buildDtoForEntity(userWithChangedStatus, UserManagedByAdminDTO.class, link);
        return new ResponseEntity<>(userManagedByAdminDTO, HttpStatus.OK);
    }

    /**
     * Ads deck to users folder
     *
     * @param userId - users id
     * @param deckId - decks id
     * @return managed by admin usersDTO
     */
    @Auditable(action = AuditingAction.ADD_DECK_TO_USER_FOLDER_ADMIN)
    @PostMapping("/api/admin/users/{userId}/deck/{deckId}")
    public ResponseEntity<UserManagedByAdminDTO> addExistingDeckToUsersFolder(@PathVariable("userId") Long userId,
                                                                              @PathVariable("deckId") Long deckId) {
        User user = userService.addExistingDeckToUsersFolder(userId, deckId);
        if (user != null) {
            Link link = linkTo(methodOn(ManageUserController.class)
                    .addExistingDeckToUsersFolder(userId, deckId)).withSelfRel();
            UserManagedByAdminDTO userManagedByAdminDTO = DTOBuilder
                    .buildDtoForEntity(user, UserManagedByAdminDTO.class, link);
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
    @DeleteMapping("/api/admin/users/{userId}/deck/{deckId}")
    public ResponseEntity<UserManagedByAdminDTO> removeDeckFromUsersFolder(@PathVariable("userId") Long userId,
                                                                           @PathVariable("deckId") Long deckId) {
        User user = userService.removeDeckFromUsersFolder(userId, deckId);
        if (user != null) {
            Link link = linkTo(methodOn(ManageUserController.class).getUserById(userId)).withSelfRel();
            UserManagedByAdminDTO userManagedByAdminDTO = DTOBuilder
                    .buildDtoForEntity(user, UserManagedByAdminDTO.class, link);
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
    @GetMapping("/api/admin/users/{userId}/decks")
    public ResponseEntity<List<DeckOfUserManagedByAdminDTO>> getAllDecksFromUsersFolder(@PathVariable("userId") Long userId) {
        List<Deck> decksFromUsersFolder = userService.getAllDecksFromUsersFolder(userId);
        Link link = linkTo(methodOn(ManageUserController.class).getAllDecksFromUsersFolder(userId)).withSelfRel();
        List<DeckOfUserManagedByAdminDTO> decksFromUsersFolderDTO = DTOBuilder
                .buildDtoListForCollection(decksFromUsersFolder, DeckOfUserManagedByAdminDTO.class, link);
        return new ResponseEntity<>(decksFromUsersFolderDTO, HttpStatus.OK);
    }
}
