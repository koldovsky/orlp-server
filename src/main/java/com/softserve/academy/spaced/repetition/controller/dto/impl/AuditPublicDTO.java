package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Audit;
import org.springframework.hateoas.Link;

public class AuditPublicDTO extends DTO<Audit> {

    public AuditPublicDTO(Audit audit, Link link) {
        super(audit, link);
    }

    public String getAccountEmail() {
        return getEntity().getAccountEmail();
    }

    public AuditingAction getAction() {
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
