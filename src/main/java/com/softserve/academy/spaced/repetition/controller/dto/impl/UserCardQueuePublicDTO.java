package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.domain.enums.UserCardQueueStatus;
import org.springframework.hateoas.Link;

import java.util.Date;

public class UserCardQueuePublicDTO extends DTO<UserCardQueue> {

    public UserCardQueuePublicDTO(UserCardQueue userCardQueue, Link link) {
        super(userCardQueue, link);
    }

    public Long getUserId() {
        return getEntity().getUserId();
    }

    public long getCardId() {
        return getEntity().getCardId();
    }

    public long getDeckId(){return  getEntity().getDeckId();}

    public UserCardQueueStatus getStatus() {
        return getEntity().getStatus();
    }

    public Date getCardDate() {
        return getEntity().getCardDate();
    }
}
