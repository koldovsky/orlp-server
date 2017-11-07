package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.domain.UserCardQueueStatus;
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
