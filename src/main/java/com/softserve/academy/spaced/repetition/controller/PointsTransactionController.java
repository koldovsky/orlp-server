package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.SendPointsToFriendDTO;
import com.softserve.academy.spaced.repetition.service.PointsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointsTransactionController {

    @Autowired
    PointsTransactionService pointsTransactionService;

    @PutMapping(value = "api/profile/wallet")
    public ResponseEntity<SendPointsToFriendDTO> sendPointsToFriend (@RequestBody SendPointsToFriendDTO sendPointsToFriendDTO) {
        SendPointsToFriendDTO pointsToFriendDTO  = pointsTransactionService.sendPointsToFriend(sendPointsToFriendDTO);
        return new ResponseEntity<>(pointsToFriendDTO, HttpStatus.OK);
    }
}
