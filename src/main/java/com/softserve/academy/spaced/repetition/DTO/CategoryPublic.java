package com.softserve.academy.spaced.repetition.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Dementhor on 7/5/2017.
 */

@JsonSerialize(as=CategoryPublic.class)
public interface CategoryPublic {

    public long getId();
    public String getName();
    public String getDescription();
    public String getImagebase64();

}
