package com.softserve.academy.spaced.repetition.security.service;

import java.io.Serializable;

/**
 * Created by jarki on 7/6/2017.
 */
public class JwtAuthenticationResponse implements Serializable{
    private static final long serialVersionUID = 125016528152483573L;

    private final String token;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
