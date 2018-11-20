package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.SendPointsToFriendDTO;

public interface PointsTransactionService {

   SendPointsToFriendDTO sendPointsToFriend (SendPointsToFriendDTO sendPointsToFriendDTO);

   int checkPointsBalance(Long userId);
}
