package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.audit.AuditingActionType;
import com.softserve.academy.spaced.repetition.domain.Audit;
import org.springframework.hateoas.Link;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.util.Date;

public class AuditPublicDTO extends DTO<Audit> {

    public AuditPublicDTO(Audit audit, Link link) {
        super(audit, link);
    }

    public String getAccountEmail() {
        return getEntity().getAccountEmail();
    }

    public AuditingActionType getAction() {
        return getEntity().getAction();
    }

    public String getIpAddress() {
        return getEntity().getIpAddress();
    }

    public String getRole() {
        return getEntity().getRole();
    }

    public String getTime() {
        return getEntity().getTime().toString();
    }
}
