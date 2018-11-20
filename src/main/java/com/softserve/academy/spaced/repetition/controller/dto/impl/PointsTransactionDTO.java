package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.PointsTransaction;
import org.springframework.hateoas.Link;

public class PointsTransactionDTO extends DTO<PointsTransaction> {
    public PointsTransactionDTO (PointsTransaction entity) {
        super(entity);
    }

    public PointsTransactionDTO(PointsTransaction entity, Link link) {
        super(entity, link);
    }

    public Integer getPoints(){
        return(this.getEntity().getPoints());
    }

    public Long getUserToId() {
        return (this.getEntity().getUserTo().getId());
    }

    public Long getUserFromId() {
        return (this.getEntity().getUserFrom().getId());
    }


}
