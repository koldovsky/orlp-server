package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.SendPointsToFriendDTO;
import com.softserve.academy.spaced.repetition.service.PointsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointsTransactionController {

    @Autowired
    PointsTransactionService pointsTransactionService;

    @PutMapping(value = "api/profile/wallet")
    public SendPointsToFriendDTO sendPointsToFriend (@RequestBody SendPointsToFriendDTO sendPointsToFriendDTO) {
        return pointsTransactionService.sendPointsToFriend(sendPointsToFriendDTO);
    }
}
