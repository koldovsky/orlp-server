package com.softserve.academy.spaced.repetition.controller.dto.simpledto;

import java.util.List;

public class CardFileDTOList {

    private List<CardFileDTO> cards;

    public List<CardFileDTO> getCards() {
        return cards;
    }

    public void setCards(List<CardFileDTO> cards) {
        this.cards = cards;
    }
}
