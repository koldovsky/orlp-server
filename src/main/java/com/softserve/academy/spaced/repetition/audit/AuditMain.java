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
import java.util.Date;

@Aspect
@Component
public class AuditMain {

    @Autowired
    AuditRepository auditRepository;

    @After("@annotation(auditable)")
    @Transactional
    public void logAuditActivity(Auditable auditable) {

        String accountEmail;
        String actionType = auditable.actionType().getActionDescription();

        try {
            JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            accountEmail = jwtUser.getUsername();
        }catch (ClassCastException ex){
            accountEmail = "GUEST";
        }

        auditRepository.save(new Audit(accountEmail, actionType, new Date()));
    }
}
