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

    @Autowired
    AuditRepository auditRepository;

    @After("@annotation(auditable)")
    @Transactional
    public void logAuditActivity(Auditable auditable) throws UnknownHostException {
        String accountEmail;
        String actionType = auditable.actionType().getActionDescription();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            if (principal.equals("anonymousUser")) {
                accountEmail = "GUEST";
            } else {
                accountEmail = String.valueOf(principal);
            }
        } else {
            JwtUser jwtUser = (JwtUser) principal;
            accountEmail = jwtUser.getUsername();
        }
        auditRepository.save(new Audit(accountEmail, actionType, new Date(), getIpAddress()));
    }

    public String getIpAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress().toString();
    }
}
