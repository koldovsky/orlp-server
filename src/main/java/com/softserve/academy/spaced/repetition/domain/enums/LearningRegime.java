package com.softserve.academy.spaced.repetition.domain.enums;

public enum LearningRegime {
    BAD_NORMAL_GOOD_STATUS_DEPENDING("BAD_NORMAL_GOOD_STATUS_DEPENDING"),
    CARDS_POSTPONING_USING_SPACED_REPETITION("CARDS_POSTPONING_USING_SPACED_REPETITION");

    private String regime;

    LearningRegime(String regime) {
        this.regime = regime;
    }

    public String getRegime() {
        return regime;
    }
}
