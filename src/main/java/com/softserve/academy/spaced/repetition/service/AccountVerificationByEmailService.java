package com.softserve.academy.spaced.repetition.service;

/**
 * Works with verification of account
 */
public interface AccountVerificationByEmailService {
    /**
     * Verifies account, and save account with updated field 'deactivated' from true to false.
     *
     * @param token must not be {@literal null}.
     */
    void accountVerification(String token);

    /**
     * Gets account email from token.
     *
     * @param token must not be {@literal null}.
     * @return string with email
     */
    String getAccountEmail(String token);
}
