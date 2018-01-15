package com.softserve.academy.spaced.repetition.utils.audit;

import com.softserve.academy.spaced.repetition.domain.Audit;
import com.softserve.academy.spaced.repetition.repository.AuditRepository;
import com.softserve.academy.spaced.repetition.security.authentification.JwtUser;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Aspect
@Component
public class AuditMain {

    private final String PRINCIPAL_ANNONYMOUS = "anonymousUser";
    private final String EMAIL_FOR_UNAUTHORIZED_USER = "ANONYMOUS";
    private final String ROLE_FOR_GUEST = "[ROLE_GUEST]";

    @Autowired
    private AuditRepository auditRepository;

    /**
     * Find out and save user's activity.
     * Save: account email, action type, time, role.
     */
    @After("@annotation(auditable)")
    public void logAuditActivity(Auditable auditable) {
        String accountEmail;
        String role;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            if (principal.equals(PRINCIPAL_ANNONYMOUS)) {
                accountEmail = EMAIL_FOR_UNAUTHORIZED_USER;
            } else {
                accountEmail = String.valueOf(principal);
            }
            role = ROLE_FOR_GUEST;
        } else {
            JwtUser jwtUser = (JwtUser) principal;
            accountEmail = jwtUser.getUsername();
            role = jwtUser.getAuthorities().toString();
        }
        auditRepository.save(new Audit(accountEmail, auditable.action(), new Date(), getIpAddress(), role));
    }

    public String getIpAddress() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
    }
}
