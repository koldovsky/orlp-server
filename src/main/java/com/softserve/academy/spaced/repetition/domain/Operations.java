package com.softserve.academy.spaced.repetition.domain;

public enum Operations {
    CREATE(0b0001),
    READ(0b0010),
    UPDATE(0b0100),
    DELETE(0b1000);

    Operations(int mask){
        this.mask=mask;
    }

    int mask;

    public int getMask(){
        return mask;
    }
}
