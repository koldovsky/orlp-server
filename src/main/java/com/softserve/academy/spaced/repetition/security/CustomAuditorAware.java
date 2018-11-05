package com.softserve.academy.spaced.repetition.security;

import com.softserve.academy.spaced.repetition.security.authentification.JwtUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomAuditorAware implements AuditorAware<Long> {
    @Override
    public Long getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return ((JwtUser) authentication.getPrincipal()).getId();
    }
}
