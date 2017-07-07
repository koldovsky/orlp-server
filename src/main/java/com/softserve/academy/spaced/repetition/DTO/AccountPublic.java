package com.softserve.academy.spaced.repetition.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Yevhen on 07.07.2017.
 */
@JsonSerialize(as = AccountPublic.class)
public interface AccountPublic {
    String getEmail();
}
