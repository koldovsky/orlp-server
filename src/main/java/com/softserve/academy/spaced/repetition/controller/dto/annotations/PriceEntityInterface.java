package com.softserve.academy.spaced.repetition.controller.dto.annotations;

import com.softserve.academy.spaced.repetition.domain.BasePrice;

public interface PriceEntityInterface {

    BasePrice getPriceObject();

    Integer getEntityPrice();
}
