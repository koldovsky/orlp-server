package com.softserve.academy.spaced.repetition.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(as = CardPublic.class)
public interface CardPublic {

    String getQuestion();

    String getAnswer();
}
