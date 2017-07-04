package com.softserve.academy.spaced.repetition.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as=DeckNameDescriptionPublic.class)
public interface DeckNameDescriptionPublic {
    String getName();

    String getDescription();
}
