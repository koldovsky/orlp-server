package com.softserve.academy.spaced.repetition.audit;

import com.softserve.academy.spaced.repetition.domain.Audit;
import com.softserve.academy.spaced.repetition.repository.AuditRepository;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@Aspect
@Component
public class AuditMain {

    private final String PRINCIPAL = "anonymousUser";
    private final String EMAIL_FOR_UNAUTHORIZED_USER = "ANONYMOUS";
    private final String ROLE_FOR_GUEST = "[ROLE_GUEST]";

    @Autowired
    AuditRepository auditRepository;

    @After("@annotation(auditable)")
    @Transactional
    public void logAuditActivity(Auditable auditable) throws UnknownHostException {
        String accountEmail;
        String role = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            if (principal.equals(PRINCIPAL)) {
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
        auditRepository.save(new Audit(accountEmail, auditable.actionType(), new Date(), getIpAddress(), role));
    }

    public String getIpAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress().toString();
    }
}
