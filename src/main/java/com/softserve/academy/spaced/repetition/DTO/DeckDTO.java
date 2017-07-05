package com.softserve.academy.spaced.repetition.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as=DeckDTO.class)
public interface DeckDTO {
    String getName();
    String getDescription();
}
