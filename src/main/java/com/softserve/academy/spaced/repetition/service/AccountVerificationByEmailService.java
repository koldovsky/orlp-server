package com.softserve.academy.spaced.repetition.service;

public interface AccountVerificationByEmailService {
    void accountVerification(String token);

    String getAccountEmail(String token);
}
